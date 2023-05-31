package idusw.springboot.controller;

import idusw.springboot.domain.Board;
import idusw.springboot.domain.Member;
import idusw.springboot.domain.PageRequestDTO;
import idusw.springboot.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/boards")
public class BoardController {
    HttpSession session = null;
    private BoardService boardService; // BoardController에서 사용할 BoardService 객체를 참조하는 변수
    public BoardController(BoardService boardService) {
        // Spring Framework가 BoardService 객체를 주입, boardService(주입될 객체의 참조변수
        this.boardService = boardService;
    }
    /*
    @GetMapping(value = {"/", ""})
    public String getBoardList(Model model) {
        model.addAttribute("key", "value");
        return "/boards/list"; //board/list.html 뷰로 전달
    }
    */

    @GetMapping("/reg-form")
    public String getRegForm(PageRequestDTO pageRequestDTO, Model model, HttpServletRequest request) {
        session = request.getSession();
        Member member = (Member) session.getAttribute("mb");
        if (member != null) {
            model.addAttribute("board", Board.builder().build());
            //System.out.println(member.getEmail());
            //model.addAttribute("member", Member.builder().build());
            return "/boards/reg-form";
        } else
            return "redirect:/members/login-form"; // 로그인이 안된 상태인 경우
    }



    @PostMapping("/")
    public String createMember(@ModelAttribute("board") Board board, Model model) { // 등록 처리 -> service -> repository -> service -> controller
        if (boardService.registerBoard(board) > 0) // 정상적으로 레코드의 변화가 발생하는 경우 영향받는 레코드 수를 반환
            return "redirect:/boards";
        else
            return "/errors/404"; // 게시물 등록 예외 처리
    }

    @PostMapping("")
    public String postBoard(@ModelAttribute("board") Board dto, Model model,  HttpServletRequest request) {
        session = request.getSession();
        Member member = (Member) session.getAttribute("mb");
      //  System.out.println(dto.getTitle() + ":" + dto.getContent());
        dto.setWriterSeq(member.getSeq());
        dto.setWriterEmail(member.getEmail());
        dto.setWriterName(member.getName());

        // login 처리하면 그냥 관계 없음
        /*
        Long seqLong = Long.valueOf(new Random().nextInt(50));
        seqLong = (seqLong == 0) ? 1L : seqLong;
        dto.setWriterSeq(seqLong);
         */
        Long bno = Long.valueOf(boardService.registerBoard(dto));

        return "redirect:/boards/" + bno; // 등록 후 상세 보기
    }

    @GetMapping("")
    public String getBoards(PageRequestDTO pageRequestDTO, Model model) {
        model.addAttribute("list", boardService.findBoardAll(pageRequestDTO));
        return "/boards/list";
    }

    @GetMapping("/{bno}")
    public String getBoardByBno(@PathVariable("bno") Long bno, Model model) {
        // Long bno 값을 사용하는 방식을 Board 객체에 bno를 설정하여 사용하는 방식으로 변경
        Board board = boardService.findBoardById(Board.builder().bno(bno).build());
        boardService.updateBoard(board);
        model.addAttribute("dto", boardService.findBoardById(board));
        return "/boards/read";
    }

    @GetMapping("/{bno}/up-form")
    public String getUpForm(@PathVariable("bno") Long bno, Model model) {
        Board board = boardService.findBoardById(Board.builder().bno(bno).build());
        model.addAttribute("board", board);
        return "/boards/upform";
    }

    @PutMapping("/{bno}")
    public String putBoard(@ModelAttribute("board") Board board, Model model) {
        boardService.updateBoard(board);
        model.addAttribute(boardService.findBoardById(board));
        return "redirect:/boards/" + board.getBno();
    }

    @GetMapping("/{bno}/del-form")
    public String getDelForm(@PathVariable("bno") Long bno, Model model) {
        Board board = boardService.findBoardById(Board.builder().bno(bno).build());
        model.addAttribute("board", board);
        return "/boards/del-form";
    }

    @DeleteMapping("/{bno}")
    public String deleteBoard(@ModelAttribute("board") Board board, Model model) {
        boardService.deleteBoard(board);
        model.addAttribute(board);
        return "redirect:/boards";
    }
}
