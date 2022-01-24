from time import sleep
import PySimpleGUI as sg
import datetime

from sender import Sender
from api_connector import ApiConnector

sg.theme('Reddit')

sender = Sender()
api = ApiConnector()

def get_shoppings_list():
    # ecrã de loading
    layout_loading = [
        [sg.Column([[sg.Text("Loading...", justification="center", font=(14))]], justification="center", vertical_alignment="center")]
    ]

    window = sg.Window("ShopAholytics", layout_loading, size=(300, 50), finalize=True)

    shoppings = None
    while True:
        shoppings = api.get_shoppings_list()

        if shoppings != None:
            break

        event, values = window.read()

        if event == sg.WIN_CLOSED:
            break

    window.close()
    return shoppings

def show_shoppings_list(shoppings):
    shoppings_menu = []
    shoppings_id = {}
    counter = 0
    for shopping in shoppings:
        shoppings_menu.append(""+shopping.get("name"))
        shoppings_id[""+shopping.get("name")] = counter
        counter += 1

    layout = [
        [sg.Text("Choose a shopping:", font=14)],
        [sg.Combo(shoppings_menu, size=(550, 50), font=(14), key="shopping")],
        [sg.Column([[sg.Button('Next')]], justification="right")]
    ]

    window = sg.Window("ShopAholytics", layout, size=(600, 100), finalize=True)
    
    shopping = ""
    while True:
        event, values = window.read()

        if event == sg.WIN_CLOSED:
            break
        # eventos das windows todas:
        elif event == 'Next':
            shopping = values["shopping"]

            if shopping ==  "":
                sg.popup('You must select a shopping!', title="Error!", font=14)

            else:
                break

    window.close()
    
    if shopping == "":
        return None
    return shoppings[shoppings_id[shopping]]

def get_sensors_list(shopping_id):
    layout_loading = [
        [sg.Column([[sg.Text("Loading...", justification="center", font=(14))]], justification="center", vertical_alignment="center")]
    ]

    window = sg.Window("ShopAholytics", layout_loading, size=(300, 50), finalize=True)

    sensors = None
    while True:
        sensors = api.get_shopping_sensors(shopping_id)

        if sensors != None:
            helpers = ["shopping", "park", "store"]

            for helper in helpers:
                for i in range(len(sensors[helper])):
                    sensors[helper][i]["sensor"]["limit"] = api.get_sensor_limit(sensors[helper][i]["sensor"]["id"])

            break

        event, values = window.read()

        if event == sg.WIN_CLOSED:
            break

    window.close()
    return sensors

def show_sensors_list(shopping, sensors_list):
    titles = ["Shopping Sensors: ", "Park Sensors: ", "Store Sensors: "]
    helpers = ["shopping", "park", "store"]

    layout = [[sg.Text("Sensors from "+shopping["name"], font=(34))]]
    sensors = []

    sensors_dic = {}
    for i in range(len(sensors_list)):
        sensors.append([sg.Text("- "+titles[i], font=14)])
        for s in sensors_list[helpers[i]]:
            sensor = s["sensor"]

            sensors_dic[sensor["id"]] = sensor
            limit = api.get_sensor_limit(sensor["id"])

            sensors.append([
                sg.Column(
                    [
                        [sg.Text(""+sensor["name"], justification="left")]
                    ]
                ), 
                sg.Column(
                    [
                        [sg.Text("Limit: "+str(limit), justification="left")]
                    ], justification="left"
                ), 
                sg.Column(
                    [
                        [sg.Button('Generate', key=str(sensor["id"]))]
                    ], justification="right"
                )
            ])

    layout.append([sg.Column(sensors, scrollable=True, size=(800, 550))])

    window = sg.Window("ShopAholytics", layout, size=(800, 600), finalize=True)

    while True:
        event, values = window.read()

        if event == sg.WIN_CLOSED:
            break

        elif window[event].get_text() == 'Generate':
            sensor_id = event

            layout_loading = [
                [sg.Column(
                    [
                        [sg.Text("How many? ", justification="left", font=(14)), sg.Input(key='number')],
                        [sg.Text("Limit: " + str(sensors_dic[int(sensor_id)]["limit"]), justification="left", font=(14))],
                        [sg.Button('Generate')]
                    ], 
                justification="center", vertical_alignment="center")]
            ]

            window_generate = sg.Window("ShopAholytics", layout_loading, size=(500, 100), finalize=True)

            number = None
            close = False
            while True:
                event, values = window_generate.read()

                if event == sg.WIN_CLOSED or close:
                    break
                elif event == 'Generate':
                    text = values['number']
                    if text == '':
                        sg.popup('You must write a number!', title="Error!", font=14)
                    else:
                        #try:
                        number = int(text)
                        # mostrar ecrã de generating e mandar mensagens pelo sender

                        if sensors_dic[int(sensor_id)]["limit"] < number:
                            sg.popup('Limit is '+ str(sensors_dic[int(sensor_id)]["limit"]) +'!', title="Error!", font=14)

                        else:

                            layout_loading = [
                                [sg.Column([[sg.Text("Generating...", justification="center", font=(14))]], justification="center", vertical_alignment="center")]
                            ]

                            window_generating = sg.Window("ShopAholytics", layout_loading, size=(300, 50), finalize=True)
                            
                            while True:
                                
                                for i in range(number):
                                    ct = datetime.datetime.now().strftime("%Y-%m-%d-%H-%M-%S")

                                    msg = {"sensor_id": sensor_id, "data": ct}
                                    sender.send(msg)

                                break

                            window_generating.close()
                            window_generate.close()
                            window.close()

                            layout_loading = [
                                [sg.Column([[sg.Text("Loading...", justification="center", font=(14))]], justification="center", vertical_alignment="center")]
                            ]

                            window = sg.Window("ShopAholytics", layout_loading, size=(300, 50), finalize=True)
                            sleep(2)
                            window.close()

                            show_sensors_list(shopping, sensors_list)

                            break
                        # except:
                        #     sg.popup('You must write a number!', title="Error!", font=14)
    window.close()

# primeiro passo, carregar os shoppings e mostrar a lista
shoppings = get_shoppings_list()
shopping = show_shoppings_list(shoppings)

if shopping == None:
    exit(1)

k = shopping["id"]
sensors = get_sensors_list(k)
show_sensors_list(shopping, sensors)
sender.close_connection()
