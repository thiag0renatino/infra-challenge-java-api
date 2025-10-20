package com.fiap.challenge_api.controller.web;

import com.fiap.challenge_api.dto.MarcadorFixoDTO;
import com.fiap.challenge_api.dto.MarcadorFixoResponseDTO;
import com.fiap.challenge_api.service.MarcadorFixoService;
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
@RequestMapping("/web/marcadores-fixos")
public class MarcadorFixoWebController {

    @Autowired
    private MarcadorFixoService service;

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "idMarcadorArucoFixo") String sort,
                       @RequestParam(defaultValue = "ASC") String dir,
                       Model model) {

        Sort.Direction direction = "DESC".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        Page<MarcadorFixoResponseDTO> pageMarcadores = service.findAll(pageable);
        List<Integer> pageNumbers = IntStream.range(0, pageMarcadores.getTotalPages()).boxed().toList();

        model.addAttribute("marcadores", pageMarcadores.getContent());
        model.addAttribute("page", pageMarcadores);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("size", size);

        return "web/marcadores-fixos/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new MarcadorFixoDTO());
        model.addAttribute("mode", "create");
        return "web/marcadores-fixos/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") MarcadorFixoDTO form,
                         BindingResult binding,
                         RedirectAttributes ra) {
        if (binding.hasErrors()) {
            return "web/marcadores-fixos/form";
        }
        service.insert(form);
        ra.addFlashAttribute("msg", "Marcador fixo cadastrado com sucesso!");
        return "redirect:/web/marcadores-fixos";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("msg", "Marcador fixo removido.");
        return "redirect:/web/marcadores-fixos";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            MarcadorFixoResponseDTO marcador = service.findByIdResponse(id);
            model.addAttribute("marcador", marcador);
            return "web/marcadores-fixos/detail";
        } catch (ResourceNotFoundException e) {
            ra.addFlashAttribute("msgErro", "Marcador fixo não encontrado.");
            return "redirect:/web/marcadores-fixos";
        }
    }

    // ===== Endpoints Alternativos =====

    @GetMapping("/por-patio/{patioId}")
    public String listByPatio(@PathVariable Long patioId, Model model) {
        List<MarcadorFixoResponseDTO> marcadores = service.findByPatioId(patioId);
        model.addAttribute("marcadores", marcadores);
        model.addAttribute("filtro", "Marcadores fixos do Pátio #" + patioId);

        model.addAttribute("veioDePatios", true);
        return "web/marcadores-fixos/list-simples";
    }

    @GetMapping("/buscar")
    public String buscarPorCodigo(@RequestParam String codigoAruco,
                                  Model model,
                                  RedirectAttributes ra) {
        try {
            MarcadorFixoResponseDTO marcador = service.findByCodigoAruco(codigoAruco);
            model.addAttribute("marcador", marcador);
            return "web/marcadores-fixos/detail";
        } catch (ResourceNotFoundException e) {
            ra.addFlashAttribute("msgErro", "Marcador com código ArUco '" + codigoAruco + "' não encontrado.");
            return "redirect:/web/marcadores-fixos";
        }
    }

}
