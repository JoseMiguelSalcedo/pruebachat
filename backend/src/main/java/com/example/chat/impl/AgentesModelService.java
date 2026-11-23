package com.example.chat.impl;

import com.example.chat.ModelService;
import com.example.chat.planeador.PlaneadorOrquestador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Nueva implementación de ModelService que usa el sistema multiagente.
 * @Primary hace que el ChatController reciba ESTA implementación en lugar de SpringAiModelService.
 */
@Service
@Primary
public class AgentesModelService implements ModelService {

    private static final Logger log = LoggerFactory.getLogger(AgentesModelService.class);
    private final PlaneadorOrquestador orquestador;

    public AgentesModelService(PlaneadorOrquestador orquestador) {
        this.orquestador = orquestador;
    }

    @Override
    public String generateResponse(String question, String sessionId) {
        log.info("Planeador multiagente. sessionId={}", sessionId);
        String respuesta = orquestador.planear(question);
        return respuesta == null ? "" : respuesta;
    }
}