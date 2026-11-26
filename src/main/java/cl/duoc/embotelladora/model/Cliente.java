package cl.duoc.embotelladora.model;

public class Cliente {
        private String rut;
        private String nombre;
        private String direccion;
        private String telefono;

        public Cliente(String rut, String nombre, String direccion, String telefono) {
            setRut(rut);
            setNombre(nombre);
            setDireccion(direccion);
            setTelefono(telefono);
        }

        public String getRut(){
            return rut;
        }

        public void setRut(String rut){
            if (rut == null || rut.trim().isEmpty()){
                throw new IllegalArgumentException("El rut del cliente es obligatorio");
            }
            this.rut = rut.trim().toUpperCase();
        }

        public String getNombre(){
            return nombre;
        }

        public void setNombre(String nombre) {
            if (nombre ==  null || nombre.trim().isEmpty()){
                throw new IllegalArgumentException("El nombre del cliente es obligatorio");
            }
            this.nombre = nombre.trim().toUpperCase();
        }
        public String getDireccion(){
            return direccion;
        }


        public void setDireccion(String direccion) {
            if (direccion ==  null || direccion.trim().isEmpty()){
                throw new IllegalArgumentException("La dirección del cliente es obligatoria");
            }
            this.direccion = direccion.trim().toUpperCase();
        }

        public String getTelefono(){
            return telefono;
        }

        public void setTelefono(String telefono) {
            if (telefono ==  null || telefono.trim().isEmpty()){
                throw new IllegalArgumentException("El telefono del cliente es obligatorio");
            }
            this.telefono = telefono.trim().toUpperCase();
        }

        @Override
        public String toString() {
            return rut + " - " + nombre;
        }
}
