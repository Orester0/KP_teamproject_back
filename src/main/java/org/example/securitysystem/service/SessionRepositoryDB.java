//package org.example.securitysystem.service;
//
//
//import org.example.securitysystem.model.models_db.SessionDB;
//import org.springframework.data.domain.Example;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.repository.query.FluentQuery;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.function.Function;
//
//
//public class SessionRepositoryDB {
//
//
//
//    @Override
//    public void flush() {
//
//    }
//
//    @Override
//    public <S extends SessionDB> S saveAndFlush(S entity) {
//        return null;
//    }
//
//    @Override
//    public <S extends SessionDB> List<S> saveAllAndFlush(Iterable<S> entities) {
//        return List.of();
//    }
//
//    @Override
//    public void deleteAllInBatch(Iterable<SessionDB> entities) {
//
//    }
//
//    @Override
//    public void deleteAllByIdInBatch(Iterable<Long> longs) {
//
//    }
//
//    @Override
//    public void deleteAllInBatch() {
//
//    }
//
//    @Override
//    public SessionDB getOne(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public SessionDB getById(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public SessionDB getReferenceById(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public <S extends SessionDB> Optional<S> findOne(Example<S> example) {
//        return Optional.empty();
//    }
//
//    @Override
//    public <S extends SessionDB> List<S> findAll(Example<S> example) {
//        return List.of();
//    }
//
//    @Override
//    public <S extends SessionDB> List<S> findAll(Example<S> example, Sort sort) {
//        return List.of();
//    }
//
//    @Override
//    public <S extends SessionDB> Page<S> findAll(Example<S> example, Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public <S extends SessionDB> long count(Example<S> example) {
//        return 0;
//    }
//
//    @Override
//    public <S extends SessionDB> boolean exists(Example<S> example) {
//        return false;
//    }
//
//    @Override
//    public <S extends SessionDB, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
//        return null;
//    }
//
//    @Override
//    public <S extends SessionDB> S save(S entity) {
//        return null;
//    }
//
//    @Override
//    public <S extends SessionDB> List<S> saveAll(Iterable<S> entities) {
//        return List.of();
//    }
//
//    @Override
//    public Optional<SessionDB> findById(Long aLong) {
//        return Optional.empty();
//    }
//
//    @Override
//    public boolean existsById(Long aLong) {
//        return false;
//    }
//
//    @Override
//    public List<SessionDB> findAll() {
//        return List.of();
//    }
//
//    @Override
//    public List<SessionDB> findAllById(Iterable<Long> longs) {
//        return List.of();
//    }
//
//    @Override
//    public long count() {
//        return 0;
//    }
//
//    @Override
//    public void deleteById(Long aLong) {
//
//    }
//
//    @Override
//    public void delete(SessionDB entity) {
//
//    }
//
//    @Override
//    public void deleteAllById(Iterable<? extends Long> longs) {
//
//    }
//
//    @Override
//    public void deleteAll(Iterable<? extends SessionDB> entities) {
//
//    }
//
//    @Override
//    public void deleteAll() {
//
//    }
//
//    @Override
//    public List<SessionDB> findAll(Sort sort) {
//        return List.of();
//    }
//
//    @Override
//    public Page<SessionDB> findAll(Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public Optional<SessionDB> findByName(String username) {
//        return Optional.empty();
//    }
//}
