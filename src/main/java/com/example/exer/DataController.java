/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.exer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ASUS
 */
@Controller
public class DataController {

    DataJpaController dataController = new DataJpaController();
    List<Data> data = new ArrayList<>();

    @RequestMapping("/")
    public String getData(Model model) {
        try {
            data = dataController.findDataEntities();
        } catch (Exception e) {
        }
        model.addAttribute("data", data);
        return "index";
    }

    @RequestMapping(value = "/img", method = RequestMethod.GET, produces = {
        MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE
    })
    public ResponseEntity<byte[]> getImg(@RequestParam("id") int id) throws Exception {
        Data data = dataController.findData(id);
        byte[] img = data.getFoto();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(img);
    }

    @RequestMapping("/create")
    public String createData() {
        return "create";
    }

    @PostMapping(value = "/newdata", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public String newData(@RequestParam("foto") MultipartFile f, HttpServletRequest r)
            throws ParseException, Exception {
        Data d = new Data();

        int id = Integer.parseInt(r.getParameter("id"));
        String nama = r.getParameter("nama");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(r.getParameter("tgl_lahir"));
        byte[] img = f.getBytes();
        d.setId(id);
        d.setNama(nama);
        d.setTglLahir(date);
        d.setFoto(img);

        dataController.create(d);
        return "created";
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public String deleteData(@PathVariable("id") int id) throws Exception {
        dataController.destroy(id);
        return "deleted";
    }

    @RequestMapping("/edit/{id}")
    public String updateData(@PathVariable("id") int id, Model m) throws Exception {
        Data d = dataController.findData(id);
        m.addAttribute("data", d);
        return "update";
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public String updateDummyData(@RequestParam("foto") MultipartFile f, HttpServletRequest r)
            throws ParseException, Exception {
        Data d = new Data();

        int id = Integer.parseInt(r.getParameter("id"));
        String nama = r.getParameter("nama");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(r.getParameter("tgl_lahir"));
        byte[] img = f.getBytes();
        
        d.setId(id);
        d.setNama(nama);
        d.setTglLahir(date);
        d.setFoto(img);

        dataController.edit(d);
        return "updated";
    }
}
