package deti.tqs.drinkup.controller;

import deti.tqs.drinkup.dto.ItemDto;
import deti.tqs.drinkup.service.ItemService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@CrossOrigin(origins = {"http://localhost:4200" , "https://www.drinkupstore.herokuapp.com","http://drinkupstore.herokuapp.com" })
@RequestMapping("/api/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping()
    public ResponseEntity<List<ItemDto>> getAllItems(){

        return new ResponseEntity<>(this.itemService.getAllItems(),
                HttpStatus.ACCEPTED);

    }
}
