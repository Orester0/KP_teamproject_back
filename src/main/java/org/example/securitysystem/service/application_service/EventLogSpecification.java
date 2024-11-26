package org.example.securitysystem.service.application_service;

import org.example.securitysystem.model.db_models.EventLog;
import org.example.securitysystem.model.db_models.EventLog_;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.CriteriaQuery;


public class EventLogSpecification {

    public static Specification<EventLog> withFilters(
            Long sessionId, Long floorId, Long roomId, String sensorType
    ) {
        return (Root<EventLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction(); // Create a blank condition

            // Filter by sessionId
            if (sessionId != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(EventLog_.session).get("sessionId"), sessionId));
            }

            // Filter by floorId
            if (floorId != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(EventLog_.floor).get("floorId"), floorId));
            }

            // Filter by roomId
            if (roomId != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(EventLog_.room).get("roomId"), roomId));
            }

            // Filter by sensor type
            if (sensorType != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(EventLog_.sensor).get("type"), sensorType));
            }

            return predicate;
        };
    }
}
