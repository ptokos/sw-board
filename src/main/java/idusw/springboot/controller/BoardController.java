package idusw.springboot.controller;

import idusw.springboot.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/boards")
public class BoardController {
    private BoardService boardService; // BoardController에서 사용할 BoardService 객체를 참조하는 변수
    public BoardController(BoardService boardService) {
        // Spring Framework가 BoardService 객체를 주입, boardService(주입될 객체의 참조변수
        this.boardService = boardService;
    }
    @GetMapping(value = {"/", ""})
    public String getBoardList(Model model) {
        model.addAttribute("key", "value");
        return "/boards/list"; //board/list.html 뷰로 전달
    }
}
