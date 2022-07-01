package com.godcoder.myrest.controller;


import com.godcoder.myrest.model.Board;
import com.godcoder.myrest.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
class BoardApiController {

        @Autowired
        private BoardRepository repository;

        // Aggregate root
        // tag::get-aggregate-root[]
        @GetMapping("/boards")
        List<Board> all(@RequestParam(required = false, defaultValue = "") String title,
                        @RequestParam(required = false, defaultValue = "") String content) {
            if(StringUtils.isEmpty(title) && StringUtils.isEmpty(content)){
                return repository.findAll();
            }else{
                return repository.findByTitleOrContent(title, content);
            }

        }
        // end::get-aggregate-root[]

        @PostMapping("/boards")
        Board newBoard(@RequestBody Board newBoard) {
            return repository.save(newBoard);
        }

        // Single item

        @GetMapping("/boards/{id}")
        Board one(@PathVariable Long id) {
            return repository.findById(id).orElse(null);
        }


        @PutMapping("/boards/{id}")
        Board replaceboard(@RequestBody Board newBoard, @PathVariable Long id) {

            return repository.findById(id)
                    .map(board -> {
                        board.setTitle(newBoard.getTitle());
                        board.setContent(newBoard.getContent());
                        return repository.save(board);
                    })
                    .orElseGet(() -> {
                        newBoard.setId(id);
                        return repository.save(newBoard);
                    });
        }

        @DeleteMapping("/boards/{id}")
        void deleteBoard(@PathVariable Long id) {
            repository.deleteById(id);
        }
}
