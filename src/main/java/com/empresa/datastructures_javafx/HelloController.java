package com.empresa.datastructures_javafx;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

public class HelloController {
    public Label label_count_set;

    @FXML
    private ComboBox<String> combo_jugadores;


    @FXML
    private TextField txt_jugador;

    @FXML
    private TextField txt_goles;

    @FXML
    private ListView<String> list_clasificacion;

    @FXML
    private Label label_jornada;

    private Map<String, Integer> clasificacion = new HashMap<>();
    private int jornada = 1;

    @FXML
    private Label label_date_time;

    @FXML
    private TextField txt_dato;

    @FXML
    private Button btn_sort;

    @FXML
    private ListView<String> list_datos;

    @FXML
    private TextField txt_dato_set;

    @FXML
    private ListView<String> list_datos_set;

    private ArrayList<String> datosAl = new ArrayList<String>();
    private Set<String> datosSet = new TreeSet<String>();

    @FXML
    public void initialize() {
        list_datos_set.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmación de eliminación");
                alert.setHeaderText(null);
                alert.setContentText("¿Estás seguro de que quieres eliminar este elemento?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    datosSet.remove(newSelection);
                    mostrarSet();
                }
            }
        });
        combo_jugadores.getItems().addAll("Jugador 1", "Jugador 2", "Jugador 3", "Jugador 4", "Jugador 5");
        clasificacion.put("Jugador 1", 0);
        clasificacion.put("Jugador 2", 0);
        clasificacion.put("Jugador 3", 0);
        clasificacion.put("Jugador 4", 0);
        clasificacion.put("Jugador 5", 0);
    }

    @FXML
    protected void actualizarClasificacion() {
        String jugador = combo_jugadores.getValue();
        int goles = Integer.parseInt(txt_goles.getText());
        clasificacion.put(jugador, clasificacion.get(jugador) + goles);

        List<Map.Entry<String, Integer>> list = new ArrayList<>(clasificacion.entrySet());
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        list_clasificacion.getItems().clear();
        for (int i = 0; i < 3 && i < list.size(); i++) {
            list_clasificacion.getItems().add(list.get(i).getKey() + ": " + list.get(i).getValue() + " goles");
        }

        jornada++;
        label_jornada.setText("Jornada: " + jornada);
        txt_goles.clear();
    }

    @FXML
    protected void agregarAl() {
        datosAl.add(txt_dato.getText());
        txt_dato.clear();
    }

    @FXML
    protected void mostrarAl() {
        list_datos.getItems().clear();
        list_datos.getItems().addAll(datosAl);
    }

    @FXML
    protected void agregarSet() {
        if (!datosSet.add(txt_dato_set.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("El dato ya existe, por favor ingrese uno diferente");
            alert.showAndWait();
            txt_dato_set.clear();
        } else {
            txt_dato_set.clear();
        }
    }

    @FXML
    protected void mostrarSet() {
        list_datos_set.getItems().clear();
        list_datos_set.getItems().addAll(datosSet);
        label_count_set.setText("Número de elementos: " + datosSet.size());

        // Obtener la dirección IP
        StringBuilder ips = new StringBuilder();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // Filtrar interfaces de loopback y desactivadas
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    // Buscar una dirección IP que no sea loopback
                    if (!addr.isLoopbackAddress()) {
                        String ip = addr.getHostAddress();
                        ips.append(ip).append("\n");
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        // Mostrar las direcciones IP en un Alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText("Las direcciones IP encontradas son: \n" + ips.toString());
        alert.showAndWait();
    }
    @FXML
    protected void mostrarSetOrdenado() {
        List<String> sortedList = new ArrayList<>(datosSet);
        Collections.sort(sortedList);
        list_datos_set.getItems().clear();
        list_datos_set.getItems().addAll(sortedList);
    }

    @FXML
    protected void agregarJugador() {
        String jugador = txt_jugador.getText();
        if (!clasificacion.containsKey(jugador)) {
            clasificacion.put(jugador, 0);
            combo_jugadores.getItems().add(jugador);
            txt_jugador.clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("El jugador ya existe, por favor ingrese uno diferente");
            alert.showAndWait();
            txt_jugador.clear();
        }
    }

    @FXML
    protected void agregarGoles() {
        String jugador = combo_jugadores.getValue();
        int goles = Integer.parseInt(txt_goles.getText());
        clasificacion.put(jugador, clasificacion.get(jugador) + goles);
        txt_goles.clear();
        actualizarClasificacion();
    }
}