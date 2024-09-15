package com.thecoders.cartunnbackend.notifications.application.internal.queryservices;

import com.thecoders.cartunnbackend.notifications.domain.model.aggregates.Notification;
import com.thecoders.cartunnbackend.notifications.domain.model.queries.GetAllNotificationsQuery;
import com.thecoders.cartunnbackend.notifications.domain.model.queries.GetNotificationByIdQuery;
import com.thecoders.cartunnbackend.notifications.domain.model.queries.GetNotificationByOrderIdQuery;
import com.thecoders.cartunnbackend.notifications.domain.services.NotificationQueryService;
import com.thecoders.cartunnbackend.notifications.infrastructure.persitence.jpa.repositories.NotificationRepository;
import com.thecoders.cartunnbackend.purchasing.domain.model.queries.GetAllNotificationsByOrderIdQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private final NotificationRepository notificationRepository;

    public NotificationQueryServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Optional<Notification> handle(GetNotificationByIdQuery query) {return notificationRepository.findById(query.notificationId());}
    @Override
    public List<Notification> handle(GetAllNotificationsQuery query) {return notificationRepository.findAll();}
    public List<Notification> handle(GetAllNotificationsByOrderIdQuery query) {
        return notificationRepository.findAllByOrderId(query.orderId());
    }
    public List<Notification> handle(GetNotificationByOrderIdQuery query) {
        return notificationRepository.findByOrderId(query.orderId());
    }
}
