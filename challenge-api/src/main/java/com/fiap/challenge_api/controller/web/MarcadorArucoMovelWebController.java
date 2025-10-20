package com.fiap.challenge_api.controller.web;

import com.fiap.challenge_api.dto.MarcadorArucoMovelDTO;
import com.fiap.challenge_api.dto.MarcadorArucoMovelResponseDTO;
import com.fiap.challenge_api.mapper.MarcadorArucoMovelMapper;
import com.fiap.challenge_api.service.MarcadorArucoMovelService;
import com.fiap.challenge_api.service.MotoService;
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
@RequestMapping("/web/marcadores-moveis")
public class MarcadorArucoMovelWebController {

    @Autowired
    private MarcadorArucoMovelService service;

    @Autowired
    private MotoService motoService;

    @Autowired
    private MarcadorArucoMovelMapper mapper;

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "idMarcadorMovel") String sort,
                       @RequestParam(defaultValue = "ASC") String dir,
                       Model model) {

        Sort.Direction direction = "DESC".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        Page<MarcadorArucoMovelResponseDTO> pageMarcadores = service.findAll(pageable);
        List<Integer> pageNumbers = IntStream.range(0, pageMarcadores.getTotalPages()).boxed().toList();

        model.addAttribute("marcadores", pageMarcadores.getContent());
        model.addAttribute("page", pageMarcadores);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("size", size);

        return "web/marcadores-moveis/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new MarcadorArucoMovelDTO());
        model.addAttribute("mode", "create");
        return "web/marcadores-moveis/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") MarcadorArucoMovelDTO form,
                         BindingResult binding,
                         RedirectAttributes ra) {
        if (binding.hasErrors()) {
            return "web/marcadores-moveis/form";
        }
        service.insert(form);
        ra.addFlashAttribute("msg", "Marcador móvel cadastrado com sucesso!");
        return "redirect:/web/marcadores-moveis";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            MarcadorArucoMovelResponseDTO resp = service.findById(id);
            MarcadorArucoMovelDTO form = mapper.toDtoFromResponse(resp);
            form.setIdMarcadorMovel(resp.getIdMarcadorMovel());
            form.setCodigoAruco(resp.getCodigoAruco());
            form.setDataInstalacao(resp.getDataInstalacao());
            form.setIdMoto(resp.getMoto() != null ? resp.getMoto().getIdMoto() : null);

            model.addAttribute("form", form);
            model.addAttribute("mode", "edit");
            return "web/marcadores-moveis/form";
        } catch (ResourceNotFoundException e) {
            ra.addFlashAttribute("msgErro", "Marcador móvel não encontrado.");
            return "redirect:/web/marcadores-moveis";
        }
    }


    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("form") MarcadorArucoMovelDTO form,
                         RedirectAttributes ra) {
        service.update(id, form);
        ra.addFlashAttribute("msg", "Marcador móvel atualizado com sucesso!");
        return "redirect:/web/marcadores-moveis";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("msg", "Marcador móvel removido.");
        return "redirect:/web/marcadores-moveis";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            MarcadorArucoMovelResponseDTO marcador = service.findById(id);
            model.addAttribute("marcador", marcador);
            return "web/marcadores-moveis/detail";
        } catch (ResourceNotFoundException e) {
            ra.addFlashAttribute("msgErro", "Marcador móvel não encontrado.");
            return "redirect:/web/marcadores-moveis";
        }
    }

    // ===== Endpoints Alternativos =====

    // Buscar por código ArUco (mostra detail)
    @GetMapping("/codigo")
    public String findByCodigo(@RequestParam String codigoAruco, Model model, RedirectAttributes ra) {
        try {
            MarcadorArucoMovelResponseDTO marcador = service.findByCodigoAruco(codigoAruco);
            model.addAttribute("marcador", marcador);
            return "web/marcadores-moveis/detail";
        } catch (ResourceNotFoundException e) {
            ra.addFlashAttribute("msgErro", "Marcador com código informado não encontrado.");
            return "redirect:/web/marcadores-moveis";
        }
    }

}