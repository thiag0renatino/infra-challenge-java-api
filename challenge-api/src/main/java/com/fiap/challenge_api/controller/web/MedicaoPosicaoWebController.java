package com.fiap.challenge_api.controller.web;

import com.fiap.challenge_api.dto.MedicaoPosicaoDTO;
import com.fiap.challenge_api.service.MedicaoPosicaoService;
import com.fiap.challenge_api.service.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/web/medicoes")
public class MedicaoPosicaoWebController {

    @Autowired
    private MedicaoPosicaoService service;

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "idMedicao") String sort,
                       @RequestParam(defaultValue = "ASC") String dir,
                       Model model) {

        Sort.Direction direction = "DESC".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        Page<MedicaoPosicaoDTO> pageMedicoes = service.findAll(pageable);
        List<Integer> pageNumbers = IntStream.range(0, pageMedicoes.getTotalPages()).boxed().toList();

        model.addAttribute("medicoes", pageMedicoes.getContent());
        model.addAttribute("page", pageMedicoes);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("size", size);

        return "web/medicoes/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new MedicaoPosicaoDTO());
        model.addAttribute("mode", "create");
        return "web/medicoes/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") MedicaoPosicaoDTO form,
                         BindingResult binding,
                         RedirectAttributes ra) {
        if (binding.hasErrors()) {
            return "web/medicoes/form";
        }
        service.insert(form);
        ra.addFlashAttribute("msg", "Medição cadastrada com sucesso!");
        return "redirect:/web/medicoes";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("msg", "Medição removida.");
        return "redirect:/web/medicoes";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            MedicaoPosicaoDTO medicao = service.findById(id);
            model.addAttribute("medicao", medicao);
            return "web/medicoes/detail";
        } catch (ResourceNotFoundException e) {
            ra.addFlashAttribute("msgErro", "Medição não encontrada.");
            return "redirect:/web/medicoes";
        }
    }


}
