package com.qxy.repository.impl;

import com.qxy.model.po.AiOrder;
import com.qxy.repository.AiOrderRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @Author: water
 * @Description: 订单数据访问接口实现类
 * @Date: 2025/1/22 16:44
 * @Version: 1.0
 */
@Repository
public class AiOrderRepositoryImpl implements AiOrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public AiOrder findByOrderId(String orderId) {
        return entityManager.createQuery("SELECT o FROM AiOrder o WHERE o.orderId = :orderId", AiOrder.class)
                .setParameter("orderId", orderId)
                .getSingleResult();
    }

    @Override
    public List<AiOrder> findAll() {
        return entityManager.createQuery("SELECT o FROM AiOrder o", AiOrder.class).getResultList();
    }

    @Override
    public List<AiOrder> findAll(Sort sort) {
        // 如果需要支持排序，可以在这里实现
        throw new UnsupportedOperationException("Sorting is not supported in this implementation");
    }

    @Override
    public Page<AiOrder> findAll(Pageable pageable) {
        // 如果需要支持分页，可以在这里实现
        throw new UnsupportedOperationException("Pagination is not supported in this implementation");
    }

    @Override
    public List<AiOrder> findAllById(Iterable<String> strings) {
        // 根据 ID 列表查询
        throw new UnsupportedOperationException("findAllById is not supported in this implementation");
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(o) FROM AiOrder o", Long.class).getSingleResult();
    }

    @Override
    public void deleteById(String s) {
        AiOrder order = entityManager.find(AiOrder.class, s);
        if (order != null) {
            entityManager.remove(order);
        }
    }

    @Override
    public void delete(AiOrder entity) {
        entityManager.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {
        // 根据 ID 列表删除
        throw new UnsupportedOperationException("deleteAllById is not supported in this implementation");
    }

    @Override
    public void deleteAll(Iterable<? extends AiOrder> entities) {
        for (AiOrder entity : entities) {
            entityManager.remove(entity);
        }
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM AiOrder").executeUpdate();
    }

    @Override
    public <S extends AiOrder> S save(S entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public <S extends AiOrder> List<S> saveAll(Iterable<S> entities) {
        for (S entity : entities) {
            entityManager.persist(entity);
        }
        return (List<S>) entities;
    }

    @Override
    public Optional<AiOrder> findById(String s) {
        return Optional.ofNullable(entityManager.find(AiOrder.class, s));
    }

    @Override
    public boolean existsById(String s) {
        return findById(s).isPresent();
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

    @Override
    public <S extends AiOrder> S saveAndFlush(S entity) {
        S savedEntity = save(entity);
        flush();
        return savedEntity;
    }

    @Override
    public <S extends AiOrder> List<S> saveAllAndFlush(Iterable<S> entities) {
        List<S> savedEntities = saveAll(entities);
        flush();
        return savedEntities;
    }

    @Override
    public void deleteAllInBatch(Iterable<AiOrder> entities) {
        for (AiOrder entity : entities) {
            entityManager.remove(entity);
        }
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {
        for (String id : strings) {
            deleteById(id);
        }
    }

    @Override
    public void deleteAllInBatch() {
        entityManager.createQuery("DELETE FROM AiOrder").executeUpdate();
    }

    @Override
    public AiOrder getOne(String s) {
        return entityManager.find(AiOrder.class, s);
    }

    @Override
    public AiOrder getById(String s) {
        return entityManager.find(AiOrder.class, s);
    }

    @Override
    public AiOrder getReferenceById(String s) {
        return entityManager.getReference(AiOrder.class, s);
    }

    @Override
    public <S extends AiOrder> Optional<S> findOne(Example<S> example) {
        // 如果需要支持 Example 查询，可以在这里实现
        throw new UnsupportedOperationException("Example queries are not supported in this implementation");
    }

    @Override
    public <S extends AiOrder> List<S> findAll(Example<S> example) {
        // 如果需要支持 Example 查询，可以在这里实现
        throw new UnsupportedOperationException("Example queries are not supported in this implementation");
    }

    @Override
    public <S extends AiOrder> List<S> findAll(Example<S> example, Sort sort) {
        // 如果需要支持 Example 查询和排序，可以在这里实现
        throw new UnsupportedOperationException("Example queries with sorting are not supported in this implementation");
    }

    @Override
    public <S extends AiOrder> Page<S> findAll(Example<S> example, Pageable pageable) {
        // 如果需要支持 Example 查询和分页，可以在这里实现
        throw new UnsupportedOperationException("Example queries with pagination are not supported in this implementation");
    }

    @Override
    public <S extends AiOrder> long count(Example<S> example) {
        // 如果需要支持 Example 查询计数，可以在这里实现
        throw new UnsupportedOperationException("Example queries are not supported in this implementation");
    }

    @Override
    public <S extends AiOrder> boolean exists(Example<S> example) {
        // 如果需要支持 Example 查询是否存在，可以在这里实现
        throw new UnsupportedOperationException("Example queries are not supported in this implementation");
    }

    @Override
    public <S extends AiOrder, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        // 如果需要支持复杂查询，可以在这里实现
        throw new UnsupportedOperationException("Complex queries are not supported in this implementation");
    }
}