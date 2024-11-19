package org.example;

import entity.Asistente;
import entity.Evento;
import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

import jdk.jfr.Event;
import org.hibernate.Session;

import java.sql.Date;
import java.util.List;

public class Main {
    private static Session session = null;

    public static void main(String[] args) {
        try {
            System.out.println("adad");
            session = HibernateUtil.getSession();
            //crearEventoConAsistente();
            //mostrarEventosYAsistentes();
            //buscarAsistente("Lucas");
            actualizarEvento(3, "Navidad");
            eliminarAsistente(3);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession(session);
        }
    }

    private static void eliminarAsistente(int idAsistente) {
        // Cargar la entidad que deseas eliminar
        Asistente user = session.get(Asistente.class, idAsistente);

        if (user != null) {
            // Iniciar la transacción y eliminar la entidad
            session.beginTransaction();
            session.remove(user);
            session.getTransaction().commit();

            System.out.println("Asistente eliminado correctamente.");
        } else {
            System.out.println("No se encontró el asistente con el ID: " + idAsistente);
        }
    }

    private static void actualizarEvento(int idEvento, String nombre) {
        // Cargar la entidad que deseas actualuzar
        Evento event = session.get(Evento.class, idEvento);

        if (event != null) {
            // Actualizamos la propiedad que querramos
            event.setNombre(nombre);

            // Iniciar la transacción y eliminar la entidad
            session.beginTransaction();
            session.merge(event);
            session.getTransaction().commit();

            System.out.println("Evento actualizado correctamente.");
        } else {
            System.out.println("No se encontró el evento con el ID: " + idEvento);
        }
    }

    private static void buscarAsistente(String nombre) {
        // Definir la consulta
        String hql = "FROM Asistente WHERE nombre = :nombre";

        // Ejecutar la consulta
        Asistente asistente = session.createQuery(hql, Asistente.class)
                .setParameter("nombre", nombre)
                .uniqueResult();

        if (asistente != null) {
            System.out.println("ID: " + asistente.getId() + ", Nombre: " + asistente.getNombre());
        } else {
            System.out.println("No se encontró el asistente con el nombre: " + nombre);
        }
    }

    private static void mostrarEventosYAsistentes() {
        List<Evento> dataList = session.createQuery("FROM Evento ", Evento.class).list();

        for (Evento ev : dataList) {
            System.out.println("ID: " + ev.getId() + ", Nombre: " + ev.getNombre());
        }

        List<Asistente> usersList = session.createQuery("FROM Asistente ", Asistente.class).list();

        for (Asistente u : usersList) {
            System.out.println("ID: " + u.getId() + ", Nombre: " + u.getNombre());
        }
    }

    private static void crearEventoConAsistente() {
        session.beginTransaction();

        Evento event = new Evento();
        event.setNombre("Fiesta de cumple");
        Date now = new Date(System.currentTimeMillis());
        event.setFecha(now.toLocalDate());
        session.persist(event);

        Asistente user = new Asistente();
        user.setId(event.getId());
        user.setNombre("Lucas");

        session.persist(user);

        session.getTransaction().commit();
    }
}