package util;


import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("unchecked")
public class ConexionObserver implements Observer {

    @Override
    @SuppressWarnings("unchecked")
    public void update(Observable o, Object args) {
        System.out.println("No hay conexion");
    }
}
