package com.thecoders.cartunnbackend.tunning.domain.model.aggregates;

import com.thecoders.cartunnbackend.tunning.domain.model.commands.CreateTunningTaskCommand;
import com.thecoders.cartunnbackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Entity;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

import java.time.LocalDate;
@Getter
@Entity
public class TunningTask extends AuditableAbstractAggregateRoot<TunningTask> {

    private String modifiedPart;
    private LocalDate date;
    private String status;

    public TunningTask() {
        this.modifiedPart = Strings.EMPTY;
        this.date = LocalDate.now();
        this.status = Strings.EMPTY;
    }

    public TunningTask(String modifiedPart, LocalDate date, String status) {
        this();
        this.modifiedPart = modifiedPart;
        this.date = date;
        this.status = status;
    }
    public TunningTask(CreateTunningTaskCommand command) {
        this();
        this.modifiedPart = command.modifiedPart();
        this.date = command.date();
        this.status = command.status();
    }
    public TunningTask updateInformation(String modifiedPart, LocalDate date, String status) {
        this.modifiedPart = modifiedPart;
        this.date = date;
        this.status = status;
        return this;
    }
}
