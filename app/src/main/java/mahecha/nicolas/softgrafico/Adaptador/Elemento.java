package mahecha.nicolas.softgrafico.Adaptador;

public class Elemento {

    public String imagen;
    public String texto;
    public String titulo;
    public String valor;
    public String idop;
    public String ideven;
    public long id;

    //CONSTRUCTOR2
    public Elemento(String img, String text, String titu, String valor, String idop,String ideven) {
        super();
        this.texto =text;
        this.imagen=img;
        this.titulo=titu;
        this.valor=valor;
        this.idop=idop;
        this.ideven=ideven;

    }

    //CONSTRUCTOR 1
    public Elemento(String img, String text, long id) {
        super();
        this.texto =text;
        this.imagen=img;
        this.id = id;
    }



    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getValor() {
        return valor;
    }


    public String getidop() {
        return idop;
    }

    public String getIdeven() {
        return ideven;
    }



}

