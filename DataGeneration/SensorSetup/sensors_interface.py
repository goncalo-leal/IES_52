import PySimpleGUI as sg

sg.theme('Reddit')

def get_loading():
    # ecrã de loading
    layout_loading = [
        [sg.Column([[sg.Text("Loading...", justification="center", font=(14))]], justification="center", vertical_alignment="center")]
    ]

    return sg.Window("ShopAholytics", layout_loading, size=(300, 50), finalize=True)

def get_home(shoppings):
    # Mostrar uma lista de todos os shoppings registados
    shoppings_menu = []
    for key in shoppings.keys():
        shoppings_menu.append(""+shoppings.get(key))

    layout = [
        [sg.Text("Choose a shopping:")],
        [sg.Combo(shoppings_menu, size=(550, 50), font=(14), key="shopping")],
        [sg.Column([[sg.Button('Next')]], justification="right")]
    ]

    return sg.Window("ShopAholytics", layout, size=(600, 100), finalize=True)

def get_sensors(shopping, shopping_sensors, park_sensors, store_sensors):
    # mostrar todos os sensores de um shopping
    layout = [[sg.Text("Sensors from "+shopping)]]
    sensors = []

    sensors.append([sg.Text("Shopping Sensors: ")])
    for key in shopping_sensors.keys():
        sensors.append([sg.Text(""+shopping_sensors.get(key)), sg.Button("Generate", key=key)])

    sensors.append([sg.Text("Park Sensors: ")])
    for key in park_sensors.keys():
        sensors.append([sg.Text(""+park_sensors.get(key)), sg.Button("Generate", key=key)])

    sensors.append([sg.Text("Shop Sensors: ")])
    for key in store_sensors.keys():
        sensors.append([sg.Text(""+store_sensors.get(key)), sg.Button("Generate", key=key)])

    layout.append([sg.Column(sensors, scrollable=True)])

    return sg.Window("ShopAholytics", layout, size=(600, 500), finalize=True)   

shoppings = None
# Ir buscar a lista de shoppings à api
# shopping devia ser uma classe para facilitar
shoppings = {
    1: 'Shopping1',
    2: 'Shopping2',
    3: 'Shopping3',
    4: 'Shopping4',
    5: 'Shopping5'
}

# criar um classe de sensores para ter toda a sua informação mais facilmente
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

window_home, window_loading, window_sensors = get_home(shoppings), get_loading(), None

show_shoppings = True
show_sensors = False
while True:
    window, event, values = sg.read_all_windows()

    # End program if user closes window
    if event == sg.WIN_CLOSED:
        if window == window_home:       # quando fecha o ecrã principal a aplicação fecha
            break
        elif window == window_loading and show_shoppings and shoppings != None:    # se estiver a dar load e já tiver os shoppings volta para lá que é seguro
            window_loading = None
        elif window == window_loading and show_shoppings and shoppings == None:    # ainda não recebeu os shoppings
            break
        elif window == window_loading and show_sensors:
            window_sensors = get_sensors(shopping, shopping_sensors, park_sensors, store_sensors)
            window_loading = None

    # eventos das windows todas:
    elif event == 'Next':
        shopping = values["shopping"]
        # carregar as cenas do shopping da API

        # mostrar ecrã de loading
        window_loading = get_loading()
        show_shoppings = False
        show_sensors = True

window.close()