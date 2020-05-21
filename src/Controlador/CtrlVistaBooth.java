package Controlador;

import Modelo.Multiplicador;
import Vista.VsitaBooth;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class CtrlVistaBooth implements ActionListener, KeyListener {

    private VsitaBooth vista;
    private Multiplicador modelo;
    private DefaultTableModel modeloTabla;
    private DefaultTableModel modeloConfi;
    private String[] encabezado = {"ITEM", "PRIMEROS BITS", "BITS AGREGADOS", "BIT EXTRA"};
    private String[] encaConfi = {"ITEM", "BITS"};
    private Integer numBits;
    private Integer numeroA;
    private Integer numeroB;
    private int step;
    private int tamProceso;
    private ArrayList<String[]> respuesta;

    public CtrlVistaBooth(VsitaBooth view) {
        this.vista = view;
        init();
    }

    /*
    Inicializar la configuración inicial de las ventanas, así como tambien los respectivos listeners y demás
     */
    private void init() {
        vista.setVisible(true);
        vista.setLocationRelativeTo(null);
        vista.setTitle("Algoritmo de Booth - JOSÉ LEÓN, LUIS SOTAMBA");
        vista.setResizable(false);
        vista.btnAdelante.addActionListener(this);
        vista.btnAtras.addActionListener(this);
        vista.btnIniciar.addActionListener(this);
        vista.campoNumA.addKeyListener(this);
        vista.campoNumB.addKeyListener(this);
        vista.campoNumBits1.addKeyListener(this);
        vista.radioBtn.addActionListener(this);
        vista.radioBtnStepbyStep.addActionListener(this);
        modeloTabla = new DefaultTableModel();
        modeloConfi = new DefaultTableModel();
        modeloConfi.setColumnIdentifiers(encaConfi);
        modeloTabla.setColumnIdentifiers(encabezado);
        vista.tablaProceso.setModel(modeloTabla);
        vista.tablaConfInicial.setModel(modeloConfi);
        vista.btnAdelante.setEnabled(false);
        vista.btnAtras.setEnabled(false);
        vista.tablaConfInicial.setEnabled(false);
        vista.tablaProceso.setEnabled(false);
        vista.tablaProceso.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int lastIndex = vista.tablaProceso.getRowCount() - 1;
                vista.tablaProceso.changeSelection(lastIndex, 0, false, false);
            }
        });
    }

    private boolean verificacionCampos() {
        if (!vista.radioBtn.isSelected() && !vista.radioBtnStepbyStep.isSelected()) {
            return false;
        }
        if (vista.campoNumA.getText().trim().isEmpty() || vista.campoNumB.getText().trim().isEmpty()) {
            return false;
        }
        return true;
    }

    /*
    Fucnión que recibe un string para mostrar mensaje como información
     */
    private void mostrarInformacion(String sms) {
        JOptionPane.showMessageDialog(vista, sms);
    }

    /*
    Función que muestra información esperando una respuesta de confirmación
     */
    private int confirmarDatos(String sms, String title) {
        return JOptionPane.showConfirmDialog(vista, sms, title, 0);
    }

    /*
    Proceso para desplegar el proceso del algoritmo de booth.
     */
    private int ejecutarVisualizacion(int paso) {
        int cont = 0;
        String numIt = "";
        boolean permitir;
        boolean finalizado = false;
        int contStrings = 0;
        for (String[] l : respuesta) {
            if (contStrings > paso) {
                continue;
            }
            permitir = false;
            for (int i = 0; i < l.length; i++) {
                if (cont == 0) {
                    cont++;
                    continue;
                }
                /*
                Si obtengo la etiqueta resultados, significa que el proceso ya termino
                 */
                if (l[i].equalsIgnoreCase("RESULTADOS")) {
                    finalizado = true;
                    cont++;
                    continue;
                }
                if (cont <= 3) {
                    modeloConfi.addRow(l[i].split(" "));
                } else {
                    if (l.length <= 1 && !finalizado) {
                        //Obtengo el valor de la iteración
                        String[] temp = l[i].split(" ");
                        System.out.println("LENGTH: " + temp.length + " temp: " + temp[1]);
                        numIt = temp[1];
                        vista.txt_Iteracion.setText(String.valueOf(numIt));
                        //-----------------------------------------------------------------------------------
                        modeloTabla.addRow(new String[]{"ITERACIÓN " + numIt, "", "", ""});
                        //-----------------------------------------------------------------------------------
                        cont++;
                        continue;
                    } else {
                        if (finalizado) {
                            //Obtengo los resultados del proceso
                            System.out.println("l: " + l[0] + " arraysize: " + respuesta.get(respuesta.size() - 2)[0] + " tam Array: " + respuesta.size() + " contString: " + contStrings);
                            if (contStrings <= respuesta.size() - 2) {
                                vista.txt_Res_Binario.setText(l[0].split(" ")[2]);
                            } else {
                                vista.txt_Res_Decimal.setText(l[0].split(" ")[2]);
                            }
                        }
                    }
                    permitir = true;
                }
                cont++;
                //System.out.print(l[i]+" |||| ");
            }
            if (permitir && !finalizado) {
                if (l[0].equalsIgnoreCase("P <- P + S")) {
                    vista.txt_Caso.setText("[1 0] -> P = P + S");
                } else if (l[0].equalsIgnoreCase("P <- P + A")) {
                    vista.txt_Caso.setText("[0 1] -> P = P + A");
                } else if (l[0].equalsIgnoreCase("P <- P >> 1")) {
                    vista.txt_Caso.setText("Corrimiento a la derecha");
                } else {
                    vista.txt_Caso.setText("No realizar ninguna acción");
                }
                modeloTabla.addRow(l);
            }
            contStrings++;
            System.out.println("");
        }

        System.out.println("Termine");
        return 0;
    }

    /*
    Eliminar modelos
     */
    private void eliminarModelos() {
        System.out.println("confi: " + modeloConfi.getRowCount() + " proceso: " + modeloTabla.getRowCount());
        while (modeloConfi.getRowCount() > 0) {
            modeloConfi.removeRow(0);
        }

        while (modeloTabla.getRowCount() > 0) {
            modeloTabla.removeRow(0);
        }
        vista.txt_Caso.setText("........");
        vista.txt_Iteracion.setText("0");
        vista.txt_Res_Binario.setText("0");
        vista.txt_Res_Decimal.setText("0");
    }

    /*
    Escucho por los eventos que se generan al presionar los botones
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnIniciar) {
            //Verifcación de campos
            if (verificacionCampos()) {
                //Confirmación de datos ingresados
                if (confirmarDatos("Está seguro de empezar el proceso del algoritmo.", "Verificación de datos") == 0) {
                    //Empezar el proceso

                    eliminarModelos();
                    modelo = new Multiplicador(Integer.valueOf(vista.campoNumA.getText().trim()),
                            Integer.valueOf(vista.campoNumB.getText().trim()), (Integer) vista.campoNumBits1.getValue());

                    ArrayList<ArrayList<String[]>> res = modelo.multiplicarPasoAPaso();
                    respuesta = modelo.desplegarProceso();
                    if (res == null) {
                        mostrarInformacion("No se pudo realizar el proceso\nAl menos uno de los FACTORES o el PRODUCTO no se puede representar en complemento a 2 con TAN SOLO " + modelo.getCantidadDeBits() + " bits");
                        return;
                    }
                    tamProceso = respuesta.size();
                    if (vista.radioBtn.isSelected()) {
                        vista.btnAdelante.setEnabled(false);
                        vista.btnIniciar.setEnabled(true);
                        vista.btnAtras.setEnabled(false);
                        ejecutarVisualizacion(tamProceso);
                    } else {
                        step = 0;
                        vista.btnAdelante.setEnabled(true);
                        vista.btnIniciar.setEnabled(false);
                        vista.btnAtras.setEnabled(false);
                    }

                }
            } else {
                mostrarInformacion("Verificar que todos los campos esten seleccionados o ingresados correctamente");
            }
        } else if (e.getSource() == vista.btnAdelante) {

            System.out.println("Adelante");
            if (step < tamProceso - 1) {
                eliminarModelos();
                step++;
                ejecutarVisualizacion(step);
                vista.btnIniciar.setEnabled(false);
                vista.btnAtras.setEnabled(true);
                vista.btnIniciar.setEnabled(false);
            }
            if (step == tamProceso - 1) {
                vista.btnAdelante.setEnabled(false);
                vista.btnIniciar.setEnabled(true);
                vista.btnAtras.setEnabled(true);
            }

        } else if (e.getSource() == vista.btnAtras) {
            System.out.println("Atrás");
            if (step > 0) {
                eliminarModelos();
                step--;
                ejecutarVisualizacion(step);
                vista.btnIniciar.setEnabled(false);
                vista.btnAdelante.setEnabled(true);
            }
            if (step == 0) {
                vista.btnAtras.setEnabled(false);
                vista.btnAdelante.setEnabled(true);
                vista.btnIniciar.setEnabled(false);
            }
            if (step < 0) {
                vista.btnAdelante.setEnabled(false);
                vista.btnIniciar.setEnabled(false);
            }
        } else if (e.getSource() == vista.radioBtnStepbyStep) {
            /*
            Habilito el botón presionado y deshabilito el otro radiobutton para que el
            usuario no pueda elegir dos opciones al mismo tiempo
             */
            vista.radioBtn.setSelected(false);
            vista.radioBtnStepbyStep.setSelected(true);
        } else if (e.getSource() == vista.radioBtn) {
            /*
            Habilito el botón presionado y deshabilito el otro radiobutton para que el
            usuario no pueda elegir dos opciones al mismo tiempo
             */
            vista.radioBtn.setSelected(true);
            vista.radioBtnStepbyStep.setSelected(false);
        }
    }

    /*
    Escuchadores en los campos de escritura para que se ingresen únicamente números
     */
    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println(e.getKeyChar());
        if (e.getKeyChar() < 48 || e.getKeyChar() > 57) {
            if (e.getKeyChar() == 45) {
//                long count = vista.campoNumA.getText().codePoints().filter(ch -> ch == '-').count();
//                long count2 = vista.campoNumB.getText().codePoints().filter(ch -> ch == '-').count();
//                if (e.getSource() == vista.campoNumA && count == 0 && vista.campoNumA.getText().length() == 0) {
//                    return;
//                } else if (e.getSource() == vista.campoNumB && count2 == 0 && vista.campoNumB.getText().length() == 0) {
                return;
//                }
            }
            e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //Evento no utilizado
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //Evento no utilizado
    }

}
