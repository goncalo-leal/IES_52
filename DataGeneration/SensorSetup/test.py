import PySimpleGUI as sg

layout = [[sg.Text("Hello from PySimpleGUI"), sg.Text('', key="DIALOG")], [sg.Button("OK")]]

window = sg.Window("Demo", layout)

while True:
    event, values = window.read()
    # End program if user closes window or
    # presses the OK button
    if event == "OK":
        window["DIALOG"].update("Hellllloooooooooo")
    elif event == sg.WIN_CLOSED:
        break

window.close()