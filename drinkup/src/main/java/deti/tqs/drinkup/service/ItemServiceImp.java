package deti.tqs.drinkup.service;

import deti.tqs.drinkup.dto.ItemDto;
import deti.tqs.drinkup.model.Item;
import deti.tqs.drinkup.repository.ItemRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@Transactional
public class ItemServiceImp implements ItemService{
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public List<ItemDto> getAllItems() {
        List<Item> list = itemRepository.findAll();

        List<ItemDto> ret = new ArrayList<>();

        for (var i: list) {
            ret.add(
                    new ItemDto(
                            i.getId(),
                            i.getName(),
                            i.getQuantity(),
                            i.getPrice(),
                            i.getBrand(),
                            i.getAlcoholContent(),
                            i.getDescription()
                    )
            );
        }

        return ret;
    }
}
