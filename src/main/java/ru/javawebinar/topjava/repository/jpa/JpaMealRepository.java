package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User ref = em.getReference(User.class, userId);
        meal.setUser(ref);
        if (meal.isNew()) {
            em.persist(meal);
            return meal;
        } else {
            return em.createNamedQuery(Meal.UPDATE)
                    .setParameter("id", meal.id())
                    .setParameter("user_id", userId)
                    .setParameter("calories", meal.getCalories())
                    .setParameter("description", meal.getDescription())
                    .setParameter("date_time", meal.getDateTime())
                    .executeUpdate() != 0 ? meal : null;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("user_id", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Object[]> resultList = em.createNamedQuery(Meal.GET, Object[].class)
                .setParameter("id", id)
                .setParameter("user_id", userId)
                .getResultList();
        if (resultList.size() == 0) {
            return null;
        }
        return createMeal(resultList.get(0));
    }

    @Override
    public List<Meal> getAll(int userId) {
        TypedQuery<Object[]> query = em.createNamedQuery(Meal.GET_ALL, Object[].class)
                .setParameter("user_id", userId);
        return collectQueryToList(query);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        TypedQuery<Object[]> query = em.createNamedQuery(Meal.GET_BETWEEN_HALF_OPEN, Object[].class)
                .setParameter("user_id", userId)
                .setParameter("start_date", startDateTime)
                .setParameter("end_date", endDateTime);
        return collectQueryToList(query);
    }

    private List<Meal> collectQueryToList(TypedQuery<Object[]> query) {
        List<Meal> result = new ArrayList<>();
        for (Object[] o : query.getResultList()) {
            result.add(createMeal(o));
        }
        return result;
    }

    private Meal createMeal(Object[] o) {
        return new Meal((int) o[0], (LocalDateTime) o[3], (String) o[1], (int) o[2]);
    }
}