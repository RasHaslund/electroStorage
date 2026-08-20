package com.example.electrostorage.service;

import com.example.electrostorage.model.ComponentModel;
import com.example.electrostorage.repository.ComponentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DemoStockInitializer implements CommandLineRunner {

    private final ComponentRepository componentRepository;

    public DemoStockInitializer(ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    @Override
    public void run(String... args) {
        List<ComponentModel> components = componentRepository.findAll();

        for (ComponentModel component : components) {
            if (component.getStockQuantity() != 0 || component.getDescription() == null) {
                continue;
            }

            String description = component.getDescription().toLowerCase();

            if (description.contains("lysende led")) {
                component.setStockQuantity(0);
            } else if (description.contains("led 5")
                    || description.contains("modstand")
                    || description.contains("batteriholder")
                    || description.contains("9 v batteri")
                    || description.contains("9v batteri")) {
                component.setStockQuantity(20);
                componentRepository.save(component);
            }
        }
    }
}
