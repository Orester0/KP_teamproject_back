package org.example.securitysystem.service;

import org.example.securitysystem.model.entity.Session;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

@Repository
public class SessionRepositoryStub implements SessionRepository {

    private final Map<Long, Session> sessions = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    @Override
    public Optional<Session> findByName(String name) {
        return sessions.values().stream()
                .filter(session -> session.getName().equals(name))
                .findFirst();
    }

    @Override
    public <S extends Session> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public List<Session> findAll() {
        return new ArrayList<>(sessions.values());
    }

    @Override
    public List<Session> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public Optional<Session> findById(Long id) {
        return Optional.ofNullable(sessions.get(id));
    }

    @Override
    public <S extends Session> S save(S session) {
        if (session.getId() == null) {
            session.setId(idCounter.incrementAndGet());
        }
        sessions.put(session.getId(), session);
        return session;
    }

    @Override
    public void deleteById(Long id) {
        sessions.remove(id);
    }

    // Інші методи JpaRepository можуть залишатися порожніми, якщо вони не використовуються
    @Override
    public boolean existsById(Long id) {
        return sessions.containsKey(id);
    }

    @Override
    public long count() {
        return sessions.size();
    }

    @Override
    public void delete(Session entity) {
        sessions.remove(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Session> entities) {
        entities.forEach(entity -> sessions.remove(entity.getId()));
    }

    @Override
    public void deleteAll() {
        sessions.clear();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Session> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Session> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Session> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Session getOne(Long aLong) {
        return null;
    }

    @Override
    public Session getById(Long aLong) {
        return null;
    }

    @Override
    public Session getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Session> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Session> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Session> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Session> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Session> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Session> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Session, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<Session> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Session> findAll(Pageable pageable) {
        return null;
    }

    // Інші методи JpaRepository не потрібні для цього stub
}

