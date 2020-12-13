package com.lrm.blog.web.admin;

import com.lrm.blog.po.Type;
import com.lrm.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * 增加分类
 */
@Controller
@RequestMapping("/admin")
public class TypeController {
    @Autowired
    private TypeService typeService;

    /**
     * 查看分类
     */
    @GetMapping("/types")
    public String types(@PageableDefault(size = 3,
            sort = {"id"}, direction = Sort.Direction.DESC)
                                Pageable pageable, Model model) {
        model.addAttribute("page", typeService.lisType(pageable));

        return "admin/types";

    }

    /**
     * 增加分类
     *
     * @param model
     * @return
     */
    @GetMapping("/types/input")
    public String input(Model model) {
        model.addAttribute("type",new Type());
        return "admin/types-input";

    }

    /**
     * 更新分类
     * @param id
     * @param model
     * @return
     */
    @GetMapping("types/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("type",typeService.getType(id));
        return "admin/types-input";
    }

    /**
     * 分类校验
     * @param type
     * @param result
     * @param attributes
     * @return
     */
    @PostMapping("/types")
    public String post(@Valid  Type type, BindingResult result, RedirectAttributes attributes){
        Type type1 = typeService.getTypeByName(type.getName());
        if (type1!=null){
            result.rejectValue("name","nameError","该分类不能重复分类");
        }

        if (result.hasErrors()){
            return "admin/types-input";
        }
        Type t=typeService.saveType(type);
        if (t==null){
            //
            attributes.addFlashAttribute("message","添加失败");
        }else {
            attributes.addFlashAttribute("message","添加成功");
        }return "redirect:/admin/types";
    }

    /**
     * 修改分类数据格式
     * @param type
     * @param result
     * @param id
     * @param attributes
     * @return
     */
    @PostMapping("/types/{id}")
    public String post(@Valid  Type type, BindingResult result, @PathVariable Long id, RedirectAttributes attributes){
        Type type1 = typeService.getTypeByName(type.getName());
        if (type1!=null){
            result.rejectValue("name","nameError","该分类不能分类");

        }

        if (result.hasErrors()){
            return "admin/types-input";
        }
        Type t=typeService.updateype(id,type);
        if (t==null){
            //
            attributes.addFlashAttribute("message","修改失败");
        }else {
            attributes.addFlashAttribute("message","更新成功");
        }return "redirect:/admin/types";
    }

    /**
     * 删除按钮
     *
     * @param id
     * @param attributes
     * @return
     */
    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        typeService.deleteType(id);
    attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/types";

    }
}
