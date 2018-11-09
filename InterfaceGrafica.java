
/**
 *   A classe que cuida da interface grafica com o usuario, especialmente do gerenciamento
 *   dos dois menus (Arquivo e Cores).
 */

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class InterfaceGrafica extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private Color corDeBordaAtual;
    private Color corInternaAtual;
    private PainelDeDesenho painel; //painel onde o desenho e feito 
    private JMenuItem novo, abrir, salvar, sair, mudaCorInterna, mudaCorDeBorda; //itens de menu
    private String ultimoDiretorioAcessado; //memoriza em qual diret�rio houve a ultima operacao de gravacao ou leitura

    /**
     * Constroi um objeto InterfaceGrafica
     */
    public InterfaceGrafica() {
        super("MiniDraw2 versao 3.0 - by Grupo 2");
        setSize(750, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        corDeBordaAtual = Color.BLACK; //Preto
        corInternaAtual = Color.WHITE; //Branco

        JMenu menuArquivo = criaMenuArquivo();
        JMenu menuDeCores = criaMenuDeCores();
        JMenuBar barraDeMenus = new JMenuBar();
        barraDeMenus.add(menuArquivo);
        barraDeMenus.add(menuDeCores);
        setJMenuBar(barraDeMenus);

        painel = new PainelDeDesenho(this);
        getContentPane().add(painel, BorderLayout.CENTER);

        ultimoDiretorioAcessado = "."; //O ponto faz alusao ao diretorio corrente
    }

    private JMenu criaMenuArquivo() {
        JMenu menuArquivo = new JMenu("Arquivo");
        novo = new JMenuItem("Novo Desenho"); //Cria um item de menu para comecar uma nova figura
        novo.addActionListener(this); //Configura o ouvinte de acao
        menuArquivo.add(novo); //Adiciona o item "Novo Desenho" ao Menu

        abrir = new JMenuItem("Abrir");
        abrir.addActionListener(this); //Configura o ouvinte de acao
        menuArquivo.add(abrir); //Adiciona o item "Abrir" ao Menu

        salvar = new JMenuItem("Salvar");
        salvar.addActionListener(this); //Configura o ouvinte de acao
        menuArquivo.add(salvar); //Adiciona o item "Salvar" ao Menu

        sair = new JMenuItem("Sair");
        sair.addActionListener(this); //Configura o ouvinte de acao
        menuArquivo.add(sair); //Adiciona o item "Salvar" ao Menu

        return menuArquivo;
    }

    private JMenu criaMenuDeCores() {
        JMenu menuDeCores = new JMenu("Cores");

        mudaCorInterna = new JMenuItem("Configurar Cor Interna");
        mudaCorInterna.addActionListener(this);
        menuDeCores.add(mudaCorInterna);

        mudaCorDeBorda = new JMenuItem("Configurar Cor de Borda");
        mudaCorDeBorda.addActionListener(this);
        menuDeCores.add(mudaCorDeBorda);

        return menuDeCores;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == novo) {
            novoArquivo();
        } else if (e.getSource() == abrir) {
            abrirArquivo();
        } else if (e.getSource() == salvar) {
            salvarArquivo();
        } else if (e.getSource() == sair) {
            sair();
        } else if (e.getSource() == mudaCorInterna) {
            mudarCorInterna();
        } else if (e.getSource() == mudaCorDeBorda) {
            mudarCorBorda();
        }
    }

    /**
     * Metodo para reinicializar todo o desenho corrente
     */
    private void novoArquivo() {
        String[] opcoes = {"Sim", "N�o"};
        int i = JOptionPane.showOptionDialog(this,
                "Tem certeza que deseja continuar? A figura atual ser� perdida.",
                "Novo Desenho", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opcoes, opcoes[0]);
        if (i == JOptionPane.YES_OPTION) {
            corDeBordaAtual = Color.BLACK;
            corInternaAtual = Color.WHITE;
            // Limpa a pilha de figuras e a tela. Obs.: Verificar array de pontos.
            painel.setPilha(new ArrayList<>());
            painel.repaint();
        }
    }

    /**
     * Metodo para ler um arquivo de desenho
     */
    @SuppressWarnings("unchecked")
    private void abrirArquivo() {
        JFileChooser escolhedorDeArquivos = new JFileChooser(ultimoDiretorioAcessado);
        escolhedorDeArquivos.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".mdr") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "*.mdr - Arquivos do MiniDraw2";
            }
        });
        int situacao = escolhedorDeArquivos.showOpenDialog(this);
        if (situacao == JFileChooser.APPROVE_OPTION) {
            ultimoDiretorioAcessado = escolhedorDeArquivos.getCurrentDirectory().toString();
            String nomeDoArquivo = escolhedorDeArquivos.getSelectedFile().getAbsolutePath();
            try {
                FileInputStream fis = new FileInputStream(nomeDoArquivo);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Object obj = ois.readObject();
                fis.close();
                ois.close();
                painel.setPilha((ArrayList<ArrayList<Point>>) obj);
                painel.setPontos((ArrayList<Point>) obj);
                corDeBordaAtual = Color.BLACK;
                corInternaAtual = Color.WHITE;
                painel.repaint();
            } catch (FileNotFoundException exc) {
                exc.printStackTrace();
            } catch (IOException exc) {
                exc.printStackTrace();
            } catch (ClassNotFoundException exc) {
                exc.printStackTrace();
            }
        }
    }

    /**
     * Metodo para gravar um arquivo de desenho
     */
    private void salvarArquivo() {
        String nomeDoArquivo;
        JFileChooser escolhedorDeArquivos = new JFileChooser(ultimoDiretorioAcessado);
        escolhedorDeArquivos.setFileFilter(new FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".mdr") || f.isDirectory();
            }

            public String getDescription() {
                return "*.mdr - Arquivos do MiniDraw2";
            }
        });
        int situacao = escolhedorDeArquivos.showSaveDialog(this);
        if (situacao == JFileChooser.APPROVE_OPTION) {
            ultimoDiretorioAcessado = escolhedorDeArquivos.getCurrentDirectory().toString();
            nomeDoArquivo = escolhedorDeArquivos.getSelectedFile().getAbsolutePath();
            if (!nomeDoArquivo.toLowerCase().endsWith(".mdr")) {
                nomeDoArquivo += ".mdr";
            }
            try {
                FileOutputStream fos = new FileOutputStream(nomeDoArquivo);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(painel.getPilha());
                oos.writeObject(painel.getPontos());
                fos.close();
                oos.close();
            } catch (FileNotFoundException exc) {
                exc.printStackTrace();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }
        if (situacao == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(this, "Arquivo n�o salvo",
                    "Salvar", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Metodo para encerrar a aplicacao
     */
    private void sair() {
        String[] opcoes = {"Sim", "N�o"};
        int i = JOptionPane.showOptionDialog(this, "Tem certeza que deseja sair?",
                "Sair", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (i == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    /**
     * Exibe a caixa de dialogo de selecao de cores e, em seguida, configura a
     * cor interna de cada um dos prototipos de figuras. A exibicao e entao
     * redesenhada.
     */
    private void mudarCorInterna() {
        corInternaAtual = JColorChooser.showDialog(this, "Selecionar Cor Interna", corInternaAtual);
        if (corInternaAtual == null) {
            corInternaAtual = Color.WHITE;
        }

        repaint();
    }

    /**
     * Exibe a caixa de dialogo de selecao de cores e, em seguida, configura a
     * cor de borda de cada um dos prototipos de figuras. A exibicao e entao
     * redesenhada.
     */
    private void mudarCorBorda() {
        corDeBordaAtual = JColorChooser.showDialog(this, "Selecionar Cor de Borda", corDeBordaAtual);
        if (corDeBordaAtual == null) {
            corDeBordaAtual = Color.BLACK;
        }

        repaint();
    }

    public Color getCorAtual() {
        return corDeBordaAtual;
    }
}
