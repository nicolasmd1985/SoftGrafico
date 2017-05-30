package mahecha.nicolas.softgrafico.Adaptador;

public class Elemento {

    public String imagen;
    public String nombre;
    public String id_dispositivo;
    public String fecha;
    public String plano;
    public String posx;
    public String posy;
    public String id_evento;
    public long id;

    //CONSTRUCTOR
    public Elemento(String imagen, String nombre, String id_dispositivo, String fecha, String plano,String posx,String posy,String id_evento) {
        super();
        this.imagen=imagen;
        this.nombre =nombre;
        this.id_dispositivo=id_dispositivo;
        this.fecha=fecha;
        this.plano=plano;
        this.posx=posx;
        this.posy=posy;
        this.id_evento=id_evento;

    }





    public String getImagen() {
        return imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getId_dispositivo() {
        return id_dispositivo;
    }

    public String getFecha() {
        return fecha;
    }

    public String getPlano() {return plano;}

    public String getPosx() {
        return posx;
    }

    public String getPosy() {
        return posy;
    }

    public String getId_evento() {
        return id_evento;
    }

    public long getId() {return id;}



}

