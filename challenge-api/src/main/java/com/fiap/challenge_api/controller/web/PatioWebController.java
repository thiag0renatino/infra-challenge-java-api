package com.fiap.challenge_api.controller.web;

import com.fiap.challenge_api.dto.MotoDTO;
import com.fiap.challenge_api.dto.PatioDTO;
import com.fiap.challenge_api.service.MotoService;
import com.fiap.challenge_api.service.PatioService;
import com.fiap.challenge_api.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/web/patios")
public class PatioWebController {

    @Autowired
    private PatioService service;

    @Autowired
    private MotoService motoService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "idPatio") String sort,
                       @RequestParam(defaultValue = "DESC") String dir,
                       Model model) {

        Sort.Direction direction = Sort.Direction.fromString(dir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        Page<PatioDTO> pagePatios = service.findAll(pageable);

        List<Integer> pageNumbers = IntStream.range(0, Math.max(pagePatios.getTotalPages(), 1))
                .boxed().toList();

        model.addAttribute("patios", pagePatios.getContent());
        model.addAttribute("page", pagePatios);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("size", size);

        return "web/patios/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new PatioDTO());
        model.addAttribute("mode", "create");
        return "web/patios/form";
    }

    @PostMapping
    public String create(@ModelAttribute("form") PatioDTO form, RedirectAttributes ra) {
        PatioDTO created = service.insert(form);
        ra.addFlashAttribute("msg", "Pátio criado com sucesso! ID #" + created.getIdPatio());
        return "redirect:/web/patios";
    }

    @GetMapping("/{id}/motos")
    public String listarMotosHistoricoPorPatio(
            @PathVariable Long id,
            Model model,
            RedirectAttributes ra
    ) {
        try {
            PatioDTO patio = service.findById(id);
            List<MotoDTO> motos = motoService.findMotosPorHistoricoDePosicaoNoPatio(id);

            model.addAttribute("patio", patio);
            model.addAttribute("motos", motos);
            return "web/patios/motos-por-patio";

        } catch (ResourceNotFoundException e) {
            ra.addFlashAttribute("msgErro", "Pátio não encontrado.");
            return "redirect:/web/patios";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("form", service.findById(id));
        model.addAttribute("mode", "edit");
        return "web/patios/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("form") PatioDTO form,
                         RedirectAttributes ra) {
        service.update(id, form);
        ra.addFlashAttribute("msg", "Pátio atualizado com sucesso!");
        return "redirect:/web/patios";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("msg", "Pátio removido.");
        return "redirect:/web/patios";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, @RequestParam(name = "from", required = false) String from
    ) {
        model.addAttribute("patio", service.findById(id));
        model.addAttribute("from", from);
        return "web/patios/detail";
    }
}
