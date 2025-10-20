package com.fiap.challenge_api.controller.web;

import com.fiap.challenge_api.dto.MotoDTO;
import com.fiap.challenge_api.service.MotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/web/motos")
public class MotoWebController {

    @Autowired
    private MotoService service;

    // Tela incial motos (findAll)
    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "idMoto") String sort,
                       @RequestParam(defaultValue = "DESC") String dir,
                       Model model) {

        Sort.Direction direction = Sort.Direction.fromString(dir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        Page<MotoDTO> pageMotos = service.findAll(pageable);

        // conteúdo da página
        List<MotoDTO> motos = pageMotos.getContent();

        // números das páginas para o paginator
        List<Integer> pageNumbers = IntStream
                .range(0, pageMotos.getTotalPages())
                .boxed()
                .toList();

        model.addAttribute("motos", motos);
        model.addAttribute("page", pageMotos);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("size", size);

        return "web/motos/list";
    }

    // Create
    @GetMapping("/new")
    public String createForm(Model model) {
        MotoDTO form = new MotoDTO();
        form.setDataCadastro(LocalDate.now()); // default
        model.addAttribute("form", form);
        model.addAttribute("mode", "create");
        return "web/motos/form";
    }

    @PostMapping
    public String create(@ModelAttribute("form") MotoDTO form,
                         RedirectAttributes ra, Model model) {
        MotoDTO created = service.insert(form);
        ra.addFlashAttribute("msg", "Moto criada com sucesso! ID #" + created.getIdMoto());
        return "redirect:/web/motos";
    }

    // Update
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        MotoDTO form = service.findById(id);
        model.addAttribute("form", form);
        model.addAttribute("mode", "edit");
        return "web/motos/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute("form") MotoDTO form,
                         RedirectAttributes ra) {
        service.update(id, form);
        ra.addFlashAttribute("msg", "Moto atualizada com sucesso!");
        return "redirect:/web/motos";
    }

    // Delete
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("msg", "Moto removida.");
        return "redirect:/web/motos";
    }

    // FindById
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        model.addAttribute("moto", service.findById(id));
        return "web/motos/detail";
    }
}