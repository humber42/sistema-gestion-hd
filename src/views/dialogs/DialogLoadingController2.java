package views.dialogs;


import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class DialogLoadingController2 {
    @FXML
    private Label loading;

    private Process process;

    public void setProcess(Process process) {
        this.process = process;
    }

    @FXML
    private void cancelProcess() {
        this.process.destroy();
    }


    @FXML
    private void initialize() {
//        TimerTask timerTask = new TimerTask() {
//            Logger LOGGER = Logger.getLogger(this.getClass().toString());
//            private int contador = 0;
//
//            @Override
//            public void run() {
//                LOGGER.log(Level.INFO, "Numero de ejecucion {0}", contador);
//                contador++;
//                try {
//                    Thread.sleep(10000);
//                    process.destroy();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        Thread th = new Thread(timerTask);
//        th.start();
    }

    public void setLabelText(String text) {
        this.loading.setText(text);
    }
}
