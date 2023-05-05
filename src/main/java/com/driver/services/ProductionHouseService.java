package com.driver.services;


import com.driver.EntryDto.ProductionHouseEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.repository.ProductionHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProductionHouseService {

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addProductionHouseToDb(ProductionHouseEntryDto productionEntry){

        ProductionHouse productionsHouse = new ProductionHouse(productionEntry.getName());

        productionsHouse.setRatings(0.0);

        ProductionHouse addedProductionHouseToDB = productionHouseRepository.save(productionsHouse);


        return addedProductionHouseToDB.getId();
    }



}
