package com.example.chat.planeador.herramientas;

import com.example.chat.planeador.datos.DatosPlaneador;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/** Tools del AGENTE DE ACTIVIDADES (Reto Parte 7). */
@Component
public class ActividadesTools {

    private final DatosPlaneador datos;

    public ActividadesTools(DatosPlaneador datos) {
        this.datos = datos;
    }

    @Tool(description = "Recomienda actividades turísticas y planes para hacer en un destino cercano a Bogotá.")
    public String recomendarActividades(
            @ToolParam(description = "Nombre del destino, por ejemplo Suesca o Guatavita") String destino) {
        String clave = datos.buscarDestino(destino);
        if (clave == null) {
            return "No conozco el destino '%s'. Destinos válidos: %s.".formatted(destino, datos.destinosValidos());
        }
        return "Actividades recomendadas en %s: %s."
                .formatted(datos.destino(clave).nombre(), datos.actividades(clave));
    }
}