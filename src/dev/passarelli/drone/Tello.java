package dev.passarelli.drone;

import dev.passarelli.drone.exception.InvalidPortException;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tello {

    // Impostazioni Client Socket
    private int TIMEOUT_RISPOSTA = 7;

    private int TIMEOUT_TAKEOFF = 20;
    private int TEMPO_TRA_COMANDI = 3;
    private static int CONTATORE_PROVE = 3;
    private String TELLO_IP = "192.168.10.1";

    // Impostazioni Porte
    private int TELLO_PORT = 8889;

    // Impostazioni Logger

    private static Logger logger = Logger.getLogger(Tello.class.getName());

    private boolean inVolo = false;


    public Tello(String TELLO_IP) throws InvalidPortException {
        this(TELLO_IP, CONTATORE_PROVE, 8889);
    }

    public Tello(String TELLO_IP, int porta) throws InvalidPortException {
        this(TELLO_IP, CONTATORE_PROVE, porta);
    }

    public Tello(String TELLO_IP, int retryCount, int porta) throws InvalidPortException {
        this.TELLO_IP = TELLO_IP;
        this.CONTATORE_PROVE = retryCount;

        if(porta <= 0 || porta > 65535) {
            throw new InvalidPortException("Valore porta invalido: " + porta);
        }

        this.TELLO_PORT = porta;

        for(int i = 0; i < CONTATORE_PROVE; i++) {
            try {
                InetAddress telloIp = InetAddress.getByName(TELLO_IP);
                boolean eRaggiungibile = telloIp.isReachable(1000);

                if (eRaggiungibile) {
                    System.out.println(TELLO_IP + " è raggiungibile.");
                    break;
                } else {
                    System.out.println(TELLO_IP + " non è raggiungibile. Riprovo...");
                }

            } catch (Exception e) {
                System.err.println("Errore durante il tentativo di raggiungere " + TELLO_IP + ": " + e.getMessage());
                // e.printStackTrace();
            }
        }

        DatagramSocket clientSocket = null;
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException e) {
            logger.log(Level.SEVERE, "Erroer nella creazione del socket", e);
        }

    }


}
