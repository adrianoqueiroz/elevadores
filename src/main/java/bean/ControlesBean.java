/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.Custo;
import model.Elevador;
import model.Fila;
import model.Usuario;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.mindmap.DefaultMindmapNode;
import org.primefaces.model.mindmap.MindmapNode;

/**
 *
 * @author adria_000
 */
@ManagedBean
@ViewScoped
public class ControlesBean implements Serializable {

    private int qtdElevadores;
    private int qtdAndares;
    private float pesoMaximo;

    private Collection<Elevador> elevadorCollection;

    private Collection<Fila> filaPrincipalCollection;

    private Usuario novoUsuario;
    private int andarAtual;

    private MindmapNode elevadoresNode;
    private MindmapNode selectedNode;

    private int melhorElevador;

    private boolean ligarElevadores;
    
    private Collection<Integer> listaAndares;

    public ControlesBean() {
        qtdElevadores = 5;
        qtdAndares = 21;
        pesoMaximo = 650;

        ligarElevadores = true;
        this.novoUsuario = new Usuario();
        this.elevadorCollection = new ArrayList<>();

        this.filaPrincipalCollection = new ArrayList<>();
        preparaComponentes();
    }

    public void preparaElevadores() {
        elevadorCollection = new ArrayList<>();

        for (int i = 1; i <= qtdElevadores; i++) {
            Elevador elevador = new Elevador();
            elevador.setNumero(i);
            elevador.setCapacidadePeso(pesoMaximo);
            elevador.setEstado("Parado");
            elevador.setAndar(0);
            elevadorCollection.add(elevador);
        }
    }

    public void preparaMindmap(Custo melhorCusto) {
        Usuario u = melhorCusto.getUsuario();
        int valorCusto = melhorCusto.getCusto();
        Elevador e = melhorCusto.getElevador();

        elevadoresNode = new DefaultMindmapNode(u.getAndarOrigem() + "º Andar", u.getAndarOrigem() + "º Andar", "feff92", false);

        for (int i = 1; i <= qtdElevadores; i++) {
            MindmapNode novoAndar;
            if (e.getNumero() == i) {
                novoAndar = new DefaultMindmapNode("Elevador " + i, "Elevador Escolhido: " + i + ". Custo: " + valorCusto, "af3848", true);
            } else {
                novoAndar = new DefaultMindmapNode("Elevador " + i, "Elevador " + i, "6e9ebf", true);
            }

            elevadoresNode.addNode(novoAndar);
            for (int parada : e.getTodasParadas()) {
                MindmapNode nodeNovoAndar = new DefaultMindmapNode(parada + "º Andar", i, "6e9ebf", true);

                novoAndar.addNode(nodeNovoAndar);

            }

        }
    }

    public int getAndarAtual() {
        return andarAtual;
    }

    public void setAndarAtual(int andarAtual) {
        this.andarAtual = andarAtual;
    }

    public Usuario getNovoUsuario() {
        return novoUsuario;
    }

    public void setNovoUsuario(Usuario novoUsuario) {
        this.novoUsuario = novoUsuario;
    }

    public MindmapNode getElevadoresNode() {
        return elevadoresNode;
    }

    public void onNodeSelect(SelectEvent event) {
        MindmapNode node = (MindmapNode) event.getObject();

        //populate if not already loaded
        if (node.getChildren().isEmpty()) {
            Object label = node.getLabel();

            String labelProcurado;
            for (int i = 1; i <= qtdElevadores; i++) {
                labelProcurado = i + "º Andar";
                if (label.toString().equals(labelProcurado)) {
                    for (Elevador elevador : elevadorCollection) {
                        if (elevador.getNumero() == i) {
                            for (Usuario usuario : elevador.getUsuarioCollection()) {
                                MindmapNode nodeUsuario = new DefaultMindmapNode(usuario.getNome(), "Usuarios no elevador", "82c542", true);
                                node.addNode(nodeUsuario);

                            }
                        }
                    }

                }
            }

        }
    }

    public void onNodeDblselect(SelectEvent event) {
        this.selectedNode = (MindmapNode) event.getObject();
    }

    public MindmapNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(MindmapNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public int getQtdElevadores() {
        return qtdElevadores;
    }

    public void setQtdElevadores(int qtdElevadores) {
        this.qtdElevadores = qtdElevadores;
    }

    public int getQtdAndares() {
        return qtdAndares;
    }

    public void setQtdAndares(int qtdAndares) {
        this.qtdAndares = qtdAndares;
    }

    public float getPesoMaximo() {
        return pesoMaximo;
    }

    public void setPesoMaximo(float pesoMaximo) {
        this.pesoMaximo = pesoMaximo;
    }

    public Collection<Elevador> getElevadorCollection() {
        return elevadorCollection;
    }

    public void setElevadorCollection(Collection<Elevador> elevadorCollection) {
        this.elevadorCollection = elevadorCollection;
    }

    public void preparaComponentes() {
        preparaElevadores();
        //preparaMindmap();
    }

    public void melhorElevadorDisponivel() {

    }

    public void ligarDesligarElevadores() {
        ligarElevadores = !ligarElevadores;
    }

    //chamar elevador, escolher o melhor e alocar na fila de espera
    public Custo pegarElevador() {
        boolean embarcar = false;

        Custo melhorCusto;
        //escolhe o melhor elevador
        melhorCusto = melhorElevadorCusto(elevadorCollection, novoUsuario);

        if (elevadorCollection.isEmpty() || novoUsuario.getAndarOrigem() == novoUsuario.getAndarDestino()) {
            return null;
        }

        //atribuição de objetivos        
        Elevador elevador;
        elevador = melhorCusto.getElevador();
        switch (elevador.getObjetivo()) {
            case "Nenhum":
                if (novoUsuario.getAndarOrigem() > novoUsuario.getAndarDestino()) {
                    elevador.setObjetivo("Descer");
                } else if (novoUsuario.getAndarOrigem() < novoUsuario.getAndarDestino()) {
                    elevador.setObjetivo("Subir");
                }
                embarcar = true;
                break;
            case "Subir":
                if (novoUsuario.getAndarOrigem() < novoUsuario.getAndarDestino()) {
                    //se o elevador ainda não passou pelo usuario
                    if (novoUsuario.getAndarOrigem() >= elevador.getAndar()) {
                        embarcar = true;
                    }
                }
                break;
            case "Descer":
                if (novoUsuario.getAndarOrigem() > novoUsuario.getAndarDestino()) {
                    //se o elevador ainda nao passou pelo usuario
                    if (novoUsuario.getAndarOrigem() <= elevador.getAndar()) {
                        embarcar = true;
                    }
                }
                break;
        }

        if (embarcar) {
            int andarOrigem = novoUsuario.getAndarOrigem();
            int andarDestino = novoUsuario.getAndarDestino();

            if (!elevador.getParadasOrigem().contains(andarOrigem)) {
                elevador.getParadasOrigem().add(andarOrigem);
            }
            if (!elevador.getParadasDestino().contains(andarDestino)) {
                elevador.getParadasDestino().add(andarDestino);
            }

            Fila fila = new Fila();
            fila.setElevador(elevador);
            fila.setUsuario(novoUsuario);
            fila.setCusto(melhorCusto);
            filaPrincipalCollection.add(fila);
            novoUsuario = new Usuario();
        }

        preparaMindmap(melhorCusto);

        return melhorCusto;
    }

    public int getMelhorElevador() {
        return melhorElevador;
    }

    public void setMelhorElevador(int melhorElevador) {
        this.melhorElevador = melhorElevador;
    }

    public Collection<Fila> getFilaPrincipalCollection() {
        return filaPrincipalCollection;
    }

    public void atualizaEstadoAutomatico() {
        if (ligarElevadores) {
            atualizaEstado();
        }
    }

    //controlador do elevador
    public void atualizaEstado() {
        Collection<Elevador> elevadoresAtualizados = new ArrayList<>();

        try {
            //atualizar o estado de todos os elevadores
            for (Elevador elevadorAtual : elevadorCollection) {

                //verificar se nesse andar algum usuário irá descer
                //pois algum pode ter o destino antes da entrada do proximo
                //verificar em todos os usuarios se chegou no destino
                boolean desembarcou;
                boolean novoDesembarque;
                do {
                    int andarElevador = elevadorAtual.getAndar();
                    desembarcou = false;
                    novoDesembarque = false;

                    for (Usuario u : elevadorAtual.getUsuarioCollection()) {
                        if (u.getAndarDestino() == andarElevador) {
                            desembarcou = true;

                            elevadorAtual.setEstado("Desembarque");

                            elevadorAtual.getUsuarioCollection().remove(u);
                            elevadorAtual.getParadasDestino().remove(andarElevador);

                            if (elevadorAtual.getUsuarioCollection().isEmpty() && elevadorAtual.getParadasOrigem().isEmpty()) {
                                elevadorAtual.setEstado("Parado");
                                elevadorAtual.setObjetivo("Nenhum");
                            }

                        }
                    }
                    //verificar se mais alguem vai descer
                    for (Usuario u2 : elevadorAtual.getUsuarioCollection()) {
                        if (u2.getAndarDestino() == andarElevador) {
                            novoDesembarque = true;
                            break;
                        } else {
                            novoDesembarque = false;
                        }
                    }

                    if (desembarcou && !novoDesembarque) {
                        elevadorAtual.getParadasDestino().remove(andarElevador);
                    }
                } while (novoDesembarque);

                //se o elevador tem um objetivo e não foi atualizado
                if (!elevadoresAtualizados.contains(elevadorAtual) && !"Nenhum".equals(elevadorAtual.getObjetivo())) {
                    //define o estado
                    int andarElevador = elevadorAtual.getAndar();
                    int menorAndarOrigem = -1;
                    int maiorAndarOrigem = -1;

                    int menorAndarDestino = -1;
                    int maiorAndarDestino = -1;

                    boolean embarcarPassageiro = false;

                    if (elevadorAtual.getParadasOrigem() != null && elevadorAtual.getParadasOrigem().size() > 0) {
                        menorAndarOrigem = Collections.min(elevadorAtual.getParadasOrigem());
                        embarcarPassageiro = true;
                    }
                    if (elevadorAtual.getParadasOrigem() != null && elevadorAtual.getParadasOrigem().size() > 0) {
                        maiorAndarOrigem = Collections.max(elevadorAtual.getParadasOrigem());
                        embarcarPassageiro = true;
                    }

                    if (null != elevadorAtual.getObjetivo() && embarcarPassageiro) {
                        switch (elevadorAtual.getObjetivo()) {

                            case "Subir":
                                if (maiorAndarOrigem != -1 && menorAndarOrigem != -1) {
                                    if (andarElevador > menorAndarOrigem) {
                                        elevadorAtual.setEstado("Descendo");
                                        elevadorAtual.setAndar(--andarElevador);
                                    } else if (andarElevador < maiorAndarOrigem) {
                                        elevadorAtual.setEstado("Subindo");
                                        elevadorAtual.setAndar(++andarElevador);
                                    }

                                    if (andarElevador == menorAndarOrigem) {
                                        //verificar em todos os usuarios se chegou na origem
                                        //para sair da fila e entrar no elevador
                                        elevadorAtual.setEstado("Embarque");

                                        boolean novoEmbarque = false;
                                        do {
                                            for (Fila fila : filaPrincipalCollection) {
                                                Usuario u = fila.getUsuario();
                                                Elevador e = fila.getElevador();

                                                novoEmbarque = false;
                                                if (u.getAndarOrigem() == andarElevador && e.getNumero() == elevadorAtual.getNumero()) {
                                                    filaPrincipalCollection.remove(fila);
                                                    elevadorAtual.getUsuarioCollection().add(u);

                                                    //verificar se ainda existem usuarios para embarcar neste mesmo andar
                                                    //se n, remove o andar da lista
                                                    for (Fila f2 : filaPrincipalCollection) {
                                                        Usuario u2 = f2.getUsuario();
                                                        Elevador e2 = f2.getElevador();

                                                        //se tem a mesma origem e não é o mesmo usuario
                                                        if (u2.getAndarOrigem() == andarElevador && e2.getNumero() == elevadorAtual.getNumero()) {
                                                            if (!u2.getNome().equals(u.getNome())) {
                                                                novoEmbarque = true;
                                                                break;
                                                            } else {
                                                                novoEmbarque = false;
                                                            }
                                                        } else {
                                                            novoEmbarque = false;
                                                        }
                                                    }

                                                    if (!novoEmbarque) {
                                                        elevadorAtual.getParadasOrigem().remove(menorAndarOrigem);
                                                    }
                                                } else {
                                                    novoEmbarque = false;
                                                }

                                            }
                                        } while (novoEmbarque);
                                    }
                                }

                                break;

                            case "Descer":
                                if (maiorAndarOrigem != -1 && menorAndarOrigem != -1) {
                                    if (andarElevador > menorAndarOrigem) {
                                        elevadorAtual.setEstado("Descendo");
                                        elevadorAtual.setAndar(--andarElevador);
                                    } else if (andarElevador < maiorAndarOrigem) {
                                        elevadorAtual.setEstado("Subindo");
                                        elevadorAtual.setAndar(++andarElevador);
                                    }

                                    if (andarElevador == maiorAndarOrigem) {
                                        //verificar em todos os usuarios chegaram na origem
                                        //para sair da fila e entrar no elevador
                                        elevadorAtual.setEstado("Embarque");

                                        boolean novoEmbarque = false;
                                        do {
                                            for (Fila fila : filaPrincipalCollection) {
                                                Usuario u = fila.getUsuario();
                                                Elevador e = fila.getElevador();

                                                novoEmbarque = false;
                                                if (u.getAndarOrigem() == andarElevador && e.getNumero() == elevadorAtual.getNumero()) {
                                                    filaPrincipalCollection.remove(fila);
                                                    elevadorAtual.getUsuarioCollection().add(u);

                                                    //verificar se ainda existem usuarios para embarcar neste mesmo andar
                                                    //se n, remove o andar da lista
                                                    for (Fila f2 : filaPrincipalCollection) {
                                                        Usuario u2 = f2.getUsuario();
                                                        Elevador e2 = f2.getElevador();

                                                        if (u2.getAndarOrigem() == andarElevador && e2.getNumero() == elevadorAtual.getNumero()) {
                                                            if (!u2.getNome().equals(u.getNome())) {
                                                                novoEmbarque = true;
                                                                break;
                                                            } else {
                                                                novoEmbarque = false;
                                                            }
                                                        } else {
                                                            novoEmbarque = false;
                                                        }
                                                    }

                                                    if (!novoEmbarque) {
                                                        elevadorAtual.getParadasOrigem().remove(maiorAndarOrigem);
                                                    }
                                                } else {
                                                    novoEmbarque = false;
                                                }
                                            }
                                        } while (novoEmbarque);
                                    }
                                }
                                break;
                        }
                    }

                    //TESTES
                    //se todos ja entraram, passar nos destinos
                    //teste
                    embarcarPassageiro = false;

                    if (elevadorAtual.getParadasOrigem() != null && elevadorAtual.getParadasOrigem().size() > 0) {
                        embarcarPassageiro = true;
                    }
                    if (elevadorAtual.getParadasOrigem() != null && elevadorAtual.getParadasOrigem().size() > 0) {
                        embarcarPassageiro = true;
                    }

                    boolean desembarcarPassageiro = false;

                    if (elevadorAtual.getParadasDestino() != null && elevadorAtual.getParadasDestino().size() > 0) {
                        menorAndarDestino = Collections.min(elevadorAtual.getParadasDestino());
                        desembarcarPassageiro = true;
                    }
                    if (elevadorAtual.getParadasDestino() != null && elevadorAtual.getParadasDestino().size() > 0) {
                        maiorAndarDestino = Collections.max(elevadorAtual.getParadasDestino());
                        desembarcarPassageiro = true;
                    }

                    if (null != elevadorAtual.getObjetivo() && desembarcarPassageiro && !embarcarPassageiro) {
                        switch (elevadorAtual.getObjetivo()) {
                            case "Subir":
                                if (menorAndarDestino != -1 && maiorAndarDestino != -1) {
                                    if (andarElevador > menorAndarDestino) {
                                        elevadorAtual.setEstado("Descendo");
                                        elevadorAtual.setAndar(--andarElevador);
                                    } else if (andarElevador < maiorAndarDestino) {
                                        elevadorAtual.setEstado("Subindo");
                                        elevadorAtual.setAndar(++andarElevador);
                                    }
                                    if (andarElevador == menorAndarDestino) {
                                        elevadorAtual.setEstado("Desembarque");

                                    }
                                }
                                break;
                            case "Descer":
                                if (maiorAndarDestino != -1 && menorAndarDestino != -1) {
                                    if (andarElevador > menorAndarDestino) {
                                        elevadorAtual.setEstado("Descendo");
                                        elevadorAtual.setAndar(--andarElevador);
                                    } else if (andarElevador < maiorAndarDestino) {
                                        elevadorAtual.setEstado("Subindo");
                                        elevadorAtual.setAndar(++andarElevador);
                                    }
                                    if (andarElevador == maiorAndarDestino) {
                                        elevadorAtual.setEstado("Desembarque");
                                    }
                                }
                                break;
                        }
                    }

                    elevadoresAtualizados.add(elevadorAtual);
                }
            }
        } catch (Exception e) {
        }
    }

    public boolean getLigarElevadores() {
        return ligarElevadores;
    }

    public void setLigarElevadores(boolean ligarElevadores) {
        this.ligarElevadores = ligarElevadores;
    }

    // CALCULA CUSTO RETORNANDO O VALOR SOMADO 
    public Custo melhorElevadorCusto(Collection<Elevador> elevadorCollection, Usuario usuario) {

        boolean primeiraVez = true;

        Custo custo = new Custo();

        for (Elevador elevador : elevadorCollection) {
            Collection<Integer> todasParadas = elevador.getTodasParadas();
            int custoInicial;

            /*calculo do custo inicial - custo do elevador até o usuario
            custo inicial será a distancia entre o usuario e o elevador somada 
            à quantidade de paradas necessarias para atingir
            o andar de origem do usuario*/
            if (usuario.getAndarOrigem() > elevador.getAndar()) {
                custoInicial = usuario.getAndarOrigem() - elevador.getAndar();
                for (int i : todasParadas) {
                    if (todasParadas.contains(i) && i < usuario.getAndarOrigem()) {
                        custoInicial++;
                    }
                }
                custoInicial++;
            } else if (usuario.getAndarOrigem() < elevador.getAndar()) {
                custoInicial = elevador.getAndar() - usuario.getAndarOrigem();
                for (int i : todasParadas) {
                    if (todasParadas.contains(i) && i > usuario.getAndarOrigem()) {
                        custoInicial++;
                    }
                }
                custoInicial++;
            } else {
                custoInicial = 0;
            }

            int custoDestino;
            /* o custo será a soma dos proximos vertices que o usuario irá passar
            utilizando o elevador somadas à quantidade de paradas realizadas

            custo destino será a distancia até o destino 
            somado à quantidade de paradas*/
            
            //subindo
            if (usuario.getAndarOrigem() < usuario.getAndarDestino()) {
                custoDestino = usuario.getAndarDestino() - usuario.getAndarOrigem();
                for (int i : todasParadas) {
                    if (i > usuario.getAndarOrigem()) {
                        custoDestino += 1;
                    }
                }
                //custo ultima parada, o destino
                custoDestino += 1;
            } //descendo
            else if (usuario.getAndarOrigem() > usuario.getAndarDestino()) {
                custoDestino = usuario.getAndarOrigem() - usuario.getAndarDestino();
                for (int i : todasParadas) {
                    if (i < usuario.getAndarOrigem()) {
                        custoDestino += 1;
                    }
                }
                custoDestino += 1;
            } else {
                custoDestino = 0;
            }

            /*verificar se o lugar que o usuario quer descer podrá ser 
            atendido caso o elevador possua um objetivo mas
            esteja executando uma ação no sentido contrario antes 
            de buscar o objetivo*/
            boolean permitirAcesso = false;
            /*caso o usuario esteja subindo e o elevador possua um objetivo
            de descer, verificar se ele está executando uma ação prévia subida
            suficiente para deixá-lo no seu destino
            */
            //usuario subindo
            if (usuario.getAndarOrigem() < usuario.getAndarDestino()) {
                switch (elevador.getObjetivo()) {
                    case "Nenhum":
                    case "Subir":
                        permitirAcesso = true;
                        break;
                    case "Descer":
                        /*se o elevador está subindo para buscar alguem
                        verificar se o andar que ele vai é suficiente 
                        para o usuario descer*/
                        if ("Subindo".equals(elevador.getEstado())) {
                            int maxOrigem = Collections.max(elevador.getParadasOrigem());

                            if (maxOrigem >= usuario.getAndarDestino()) {
                                permitirAcesso = true;
                            }
                        }
                        break;
                }//usuario descendo
            } else if (usuario.getAndarOrigem() > usuario.getAndarDestino()) {
                switch (elevador.getObjetivo()) {
                    case "Nenhum":
                    case "Descer":

                        permitirAcesso = true;
                        break;
                    case "Subir":
                        //se o elevador está descendo para buscar alguem
                        //verificar se o andar que ele vai é suficiente 
                        //para o usuario descer
                        if ("Descendo".equals(elevador.getEstado())) {
                            int minOrigem = Collections.min(elevador.getParadasOrigem());

                            if (minOrigem <= usuario.getAndarDestino()) {
                                permitirAcesso = true;
                            }
                        }
                        break;
                }
            }
            
            //verificar se o elevador ainda suporta o peso do usuario
            if(elevador.calculaCargaRestante() - usuario.getPeso() < 0){
                permitirAcesso = false;
            }

            if (permitirAcesso) {
                //primeiro elevador
                if (primeiraVez) {
                    custo.setElevador(elevador);
                    custo.setCusto(custoInicial + custoDestino);
                    custo.setUsuario(usuario);

                    primeiraVez = false;
                } else if (custo.getCusto() > (custoInicial + custoDestino)) {
                    custo.setElevador(elevador);
                    custo.setCusto(custoInicial + custoDestino);
                    custo.setUsuario(usuario);
                }
            }
        }

        return custo;
    }

    public Collection<Integer> getListaAndares() {
        listaAndares = new ArrayList<>();
        for(int i = 0; i <= qtdAndares; i++ ){
            listaAndares.add(i);
        }
        return listaAndares;
    }

    public void setListaAndares(Collection<Integer> listaAndares) {
        this.listaAndares = listaAndares;
    }
    
    

}
