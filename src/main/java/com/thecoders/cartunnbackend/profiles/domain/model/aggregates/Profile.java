package com.thecoders.cartunnbackend.profiles.domain.model.aggregates;

import com.thecoders.cartunnbackend.profiles.domain.model.commands.CreateProfileCommand;
import com.thecoders.cartunnbackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

@Getter
@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "email_address", nullable = false)
    private String email;


    public Profile(){
        this.name = Strings.EMPTY;
        this.lastName = Strings.EMPTY;
        this.email = Strings.EMPTY;
    }

    public Profile(String name,String lastName, String email){
        this();
        this.name = name;
        this.lastName = lastName;
        this.email = email;

    }

    public Profile(CreateProfileCommand command){
        this();
        this.name = command.name();
        this.lastName = command.lastName();
        this.email = command.email();
    }

    public Profile updateInformation(String name,String lastName,String email){
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        return this;
    }

}
