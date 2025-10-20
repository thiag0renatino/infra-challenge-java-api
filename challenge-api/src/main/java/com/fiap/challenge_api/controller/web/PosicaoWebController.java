package com.fiap.challenge_api.controller.web;

import com.fiap.challenge_api.dto.MotoDTO;
import com.fiap.challenge_api.dto.PosicaoDTO;
import com.fiap.challenge_api.dto.PosicaoResponseDTO;
import com.fiap.challenge_api.service.MotoService;
import com.fiap.challenge_api.service.PatioService;
import com.fiap.challenge_api.service.PosicaoService;
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
@RequestMapping("/web/posicoes")
public class PosicaoWebController {

    @Autowired
    private PosicaoService service;

    @Autowired
    private MotoService motoService;

    @Autowired
    private PatioService patioService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "idPosicao") String sort,
                       @RequestParam(defaultValue = "ASC") String dir,
                       Model model) {

        Sort.Direction direction = "DESC".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        Page<PosicaoResponseDTO> pagePosicoes = service.findAll(pageable);

        List<Integer> pageNumbers = IntStream.range(0, pagePosicoes.getTotalPages()).boxed().toList();


        model.addAttribute("posicoes", pagePosicoes.getContent());
        model.addAttribute("page", pagePosicoes);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("size", size);

        return "web/posicoes/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new PosicaoDTO());
        model.addAttribute("mode", "create");
        return "web/posicoes/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") PosicaoDTO form,
                         BindingResult binding,
                         RedirectAttributes ra) {
        if (binding.hasErrors()) {
            return "web/posicoes/form";
        }
        service.insert(form);
        ra.addFlashAttribute("msg", "Posição registrada com sucesso!");
        return "redirect:/web/posicoes";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            PosicaoDTO form = service.findById(id);
            model.addAttribute("form", form);
            model.addAttribute("mode", "edit");
            return "web/posicoes/form";
        } catch (ResourceNotFoundException e) {
            ra.addFlashAttribute("msgErro", "Posição não encontrada.");
            return "redirect:/web/posicoes";
        }
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("form") PosicaoDTO form,
                         RedirectAttributes ra) {
        service.update(id, form);
        ra.addFlashAttribute("msg", "Posição atualizada com sucesso!");
        return "redirect:/web/posicoes";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("msg", "Posição removida.");
        return "redirect:/web/posicoes";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            PosicaoResponseDTO posicao = service.findByIdResponse(id);
            model.addAttribute("posicao", posicao);
            return "web/posicoes/detail";
        } catch (ResourceNotFoundException e) {
            ra.addFlashAttribute("msgErro", "Posição não encontrada.");
            return "redirect:/web/posicoes";
        }
    }

    // ===== Endpoints Alternativos =====

    // Listar por ID da MOTO
    @GetMapping("/moto/{motoId}")
    public String listByMoto(@PathVariable Long motoId, Model model) {
        List<PosicaoResponseDTO> posicoes = service.findByMotoId(motoId); // flat
        model.addAttribute("posicoes", posicoes);
        model.addAttribute("filtro", "Posições da Moto #" + motoId);
        return "web/posicoes/list-simples";
    }

    // Buscar MOTO por ID da Posição
    @GetMapping("/{id}/moto")
    public String buscarMotoPorPosicao(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            MotoDTO moto = service.buscarMotoPorIdPosicao(id);
            model.addAttribute("moto", moto);
            model.addAttribute("posicaoId", id);
            return "web/posicoes/moto-da-posicao";
        } catch (ResourceNotFoundException e) {
            ra.addFlashAttribute("msgErro", "Posição não encontrada.");
            return "redirect:/web/posicoes";
        }
    }

    // Histórico por Moto
    @GetMapping("/historico/{motoId}")
    public String historico(@PathVariable Long motoId, Model model) {
        List<PosicaoResponseDTO> posicoes = service.buscarHistoricoDaMoto(motoId);
        model.addAttribute("posicoes", posicoes);
        model.addAttribute("filtro", "Histórico da Moto #" + motoId);
        return "web/posicoes/list-simples";
    }

}
