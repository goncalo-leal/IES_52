import PySimpleGUI as sg

sg.theme('Reddit')

def get_shoppings_list():
    # ecrã de loading
    layout_loading = [
        [sg.Column([[sg.Text("Loading...", justification="center", font=(14))]], justification="center", vertical_alignment="center")]
    ]

    window = sg.Window("ShopAholytics", layout_loading, size=(300, 50), finalize=True)

    # while !get_shoppings(): -> Qualquer coisa assim

    while True:
        event, values = window.read()

        if event == sg.WIN_CLOSED:
            break

    shoppings = {
        1: 'Shopping1',
        2: 'Shopping2',
        3: 'Shopping3',
        4: 'Shopping4',
        5: 'Shopping5'
    }

    window.close()
    return shoppings

def show_shoppings_list(shoppings):
    shoppings_menu = []
    for key in shoppings.keys():
        shoppings_menu.append(""+shoppings.get(key))

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
    return shopping

def get_sensors_list(shopping_id):
    # ecrã de loading
    layout_loading = [
        [sg.Column([[sg.Text("Loading...", justification="center", font=(14))]], justification="center", vertical_alignment="center")]
    ]

    window = sg.Window("ShopAholytics", layout_loading, size=(300, 50), finalize=True)

    # while !get_sensors(): -> Qualquer coisa assim

    while True:
        event, values = window.read()

        if event == sg.WIN_CLOSED:
            break
    
    shopping_sensors = {
        1: 'Entrada Norte #1',
        2: 'Entrada Norte #2',
        3: 'Saída Norte #1',
        4: 'Entrada Norte #3',
        5: 'Entrada Sul #1',
        6: 'Entrada SUL #2',
    }

    park_sensors = {
        1: 'Entrada esquerda',
        2: 'Entrada direita',
        3: 'Saída direita',
        4: 'Saída esquerda',
    }

    store_sensors = {
        1: 'Entrada NIKE',
        2: 'Entrada SportZone',
        3: 'Entrada Continente',
        4: 'Saída Adidas',
        5: 'Sáida NIKE',
        6: 'Saída Continente',
        7: 'Entrada Worten',
    }

    window.close()
    return [shopping_sensors, park_sensors, store_sensors]

def show_sensors_list(shopping, sensors_list):
    titles = ["Shopping Sensors: ", "Park Sensors: ", "Shop Sensors: "]
    helpers = ["shopping", "park", "shop"]

    layout = [[sg.Text("Sensors from "+shopping)]]
    sensors = []

    for i in range(len(sensors_list)):
        sensors.append([sg.Text("- "+titles[i], font=14)])
        for key in sensors_list[i]:
            sensors.append([
                sg.Column(
                    [
                        [sg.Text(""+sensors_list[i].get(key), justification="left")]
                    ], justification="left"
                ), 
                sg.Column(
                    [
                        [sg.Button('Generate', key=helpers[i]+"_"+str(key))]
                    ], justification="right"
                )
            ])

    layout.append([sg.Column(sensors, scrollable=True, size=(600, 450))])

    window = sg.Window("ShopAholytics", layout, size=(600, 500), finalize=True)

    while True:
        event, values = window.read()

        if event == sg.WIN_CLOSED:
            break

        elif window[event].get_text() == 'Generate':
            print("generate")
            layout_loading = [
                [sg.Column(
                    [
                        [sg.Text("How many? ", justification="left", font=(14)), sg.Input(key='number')],
                        [sg.Button('Generate')]
                    ], 
                justification="center", vertical_alignment="center")]
            ]

            window_generating = sg.Window("ShopAholytics", layout_loading, size=(500, 100), finalize=True)

            number = None
            close = False
            while True:
                event, values = window_generating.read()

                if event == sg.WIN_CLOSED or close:
                    break
                elif event == 'Generate':
                    text = values['number']
                    if text == '':
                        sg.popup('You must write a number!', title="Error!", font=14)
                    else:
                        try:
                            number = int(text)
                            break
                        except:
                            sg.popup('You must write a number!', title="Error!", font=14)
            window_generating.close()

# primeiro passo, carregar os shoppings e mostrar a lista
shoppings = get_shoppings_list()
shopping = show_shoppings_list(shoppings)

k = None
for key in shoppings.keys():
    if shoppings.get(key) == shopping:
        k = key
        break

if k != None:
    sensors = get_sensors_list(k)
    show_sensors_list(shopping, sensors)
