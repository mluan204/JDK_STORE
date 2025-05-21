package com.example.Backend_IE303.controller;

import com.example.Backend_IE303.dto.ComboDTO;
import com.example.Backend_IE303.entity.Combo;
import com.example.Backend_IE303.service.ComboService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/combo")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ComboController {
    private final ComboService comboService;

    public ComboController(ComboService comboService) {
        this.comboService = comboService;
    }

    @GetMapping
    public ResponseEntity<List<List<Integer>>> getAllComboList() {
        List<List<Integer>> comboList = comboService.getAllComboList();
        return ResponseEntity.ok(comboList);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<ComboDTO>> getAllCombos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Combo> comboPage = comboService.getAllCombo(pageable);
        Page<ComboDTO> comboDTOPage = comboPage.map(ComboDTO::fromEntity);
        return ResponseEntity.ok(comboDTOPage);
    }

    @PostMapping
    public ResponseEntity<ComboDTO> createCombo(@RequestBody ComboDTO comboDTO) {
        Combo createdCombo = comboService.createCombo(comboDTO);
        return ResponseEntity.ok(ComboDTO.fromEntity(createdCombo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteCombo(@PathVariable Integer id) {
        Boolean isDeleted = comboService.deleteCombo(id);
        return ResponseEntity.ok(isDeleted);
    }
}
