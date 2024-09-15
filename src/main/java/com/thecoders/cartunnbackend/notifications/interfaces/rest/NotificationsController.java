package com.thecoders.cartunnbackend.notifications.interfaces.rest;


import com.thecoders.cartunnbackend.notifications.domain.model.commands.DeleteNotificationCommand;
import com.thecoders.cartunnbackend.notifications.domain.model.queries.GetAllNotificationsQuery;
import com.thecoders.cartunnbackend.notifications.domain.model.queries.GetNotificationByIdQuery;
import com.thecoders.cartunnbackend.notifications.domain.model.queries.GetNotificationByOrderIdQuery;
import com.thecoders.cartunnbackend.notifications.domain.services.NotificationCommandService;
import com.thecoders.cartunnbackend.notifications.domain.services.NotificationQueryService;
import com.thecoders.cartunnbackend.notifications.interfaces.rest.resources.CreateNotificationResource;
import com.thecoders.cartunnbackend.notifications.interfaces.rest.resources.NotificationResource;
import com.thecoders.cartunnbackend.notifications.interfaces.rest.resources.UpdateNotificationResource;
import com.thecoders.cartunnbackend.notifications.interfaces.rest.transform.CreateNotificationCommandFromResourceAssembler;
import com.thecoders.cartunnbackend.notifications.interfaces.rest.transform.NotificationResourceFromEntityAssembler;
import com.thecoders.cartunnbackend.notifications.interfaces.rest.transform.UpdateNotificationCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/notifications", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Notifications", description = "Notification Management Endpoints")
public class NotificationsController {
    private final NotificationCommandService notificationCommandService;
    private final NotificationQueryService notificationQueryService;

    public NotificationsController(NotificationCommandService notificationCommandService, NotificationQueryService notificationQueryService) {
        this.notificationCommandService = notificationCommandService;
        this.notificationQueryService = notificationQueryService;
    }
    @PostMapping
    public ResponseEntity<NotificationResource> createNotification(@RequestBody CreateNotificationResource createNotificationResource) {
        var createNotificationCommand = CreateNotificationCommandFromResourceAssembler.toCommandFromResource(createNotificationResource);
        var notificationId = notificationCommandService.handle(createNotificationCommand);
       System.out.println("notificationId: " + notificationId);
        var getNotificationByOrderIdQuery = new GetNotificationByOrderIdQuery(createNotificationResource.orderId());
        var notification = notificationQueryService.handle(getNotificationByOrderIdQuery);
        if (notification.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var notificationResource = NotificationResourceFromEntityAssembler.toResourceFromEntity(notification.get(0));
        return new ResponseEntity<>(notificationResource, HttpStatus.CREATED);
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResource> getNotification(@PathVariable Long notificationId) {
        var getNotificationByIdQuery = new GetNotificationByIdQuery(notificationId);
        var notification = notificationQueryService.handle(getNotificationByIdQuery);
        if (notification.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var notificationResource = NotificationResourceFromEntityAssembler.toResourceFromEntity(notification.get());
        return ResponseEntity.ok(notificationResource);
    }
    @GetMapping
    public ResponseEntity<List<NotificationResource>> getAllNotifications() {
        var getAllNotificationsQuery = new GetAllNotificationsQuery();
        var notifications = notificationQueryService.handle(getAllNotificationsQuery);
        var notificationResources = notifications.stream().map(NotificationResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(notificationResources);
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<NotificationResource> updateNotification(@PathVariable Long notificationId, @RequestBody UpdateNotificationResource updateNotificationResource) {
        var updateNotificationCommand = UpdateNotificationCommandFromResourceAssembler.toCommandFromResource(notificationId, updateNotificationResource);
        var updatedNotification = notificationCommandService.handle(updateNotificationCommand);
        if (updatedNotification.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var notificationResource = NotificationResourceFromEntityAssembler.toResourceFromEntity(updatedNotification.get());
        return ResponseEntity.ok(notificationResource);
    }
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long notificationId) {
        var deleteNotificationCommand = new DeleteNotificationCommand(notificationId);
        notificationCommandService.handle(deleteNotificationCommand);
        return ResponseEntity.ok("Notification deleted successfully");
    }
}
