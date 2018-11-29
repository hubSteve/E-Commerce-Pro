package cn.itcast.core.controller.poi;

import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.service.brand.BrandService;
import cn.itcast.core.service.itemCat.ItemCatService;
import cn.itcast.core.service.spec.SpecificationService;
import cn.itcast.core.service.template.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("import")
public class ImportController {

    @Reference
    private BrandService brandService;

    @Reference
    private SpecificationService specificationService;

    @Reference
    private TypeTemplateService typeTemplateService;

    @Reference
    private ItemCatService itemCatService;

    @RequestMapping("readBrandXls")
    public String readBrandXls(MultipartFile file){
        try {
            List<Brand> brandList=new ArrayList<>();
            InputStream is = file.getInputStream();
            BufferedInputStream bufferedInputStream=new BufferedInputStream(is);
            POIFSFileSystem fileSystem=new POIFSFileSystem(bufferedInputStream);
            HSSFWorkbook book=new HSSFWorkbook(fileSystem);
            HSSFSheet sheet=book.getSheetAt(0);

            for (int i = 1; i < sheet.getLastRowNum()+1; i++) {
                HSSFRow row = sheet.getRow(i);
                row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                String name = row.getCell(0).getStringCellValue();
                String firstChar = row.getCell(1).getStringCellValue();
                String status = row.getCell(2).getStringCellValue();
                Brand brand=new Brand();
                brand.setName(name);
                brand.setFirstChar(firstChar);
                brand.setStatus(status);

                brandList.add(brand);

            }
            for (Brand brand : brandList) {
                System.out.println(brand);
            }
            brandService.addBrandList(brandList);
            return "redirect:/admin/brand.html";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/admin/error.html";
        }
    }

    @RequestMapping("readSpecXls")
    public String readSpecXls(MultipartFile file){
        try {
            List<Specification> specificationList=new ArrayList<>();
            InputStream is = file.getInputStream();
            BufferedInputStream bufferedInputStream=new BufferedInputStream(is);
            POIFSFileSystem fileSystem=new POIFSFileSystem(bufferedInputStream);
            HSSFWorkbook book=new HSSFWorkbook(fileSystem);
            HSSFSheet sheet=book.getSheetAt(0);

            for (int i = 1; i < sheet.getLastRowNum()+1; i++) {
                HSSFRow row = sheet.getRow(i);
                row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                String specName = row.getCell(0).getStringCellValue();
                String status = row.getCell(1).getStringCellValue();
                Specification specification=new Specification();
                specification.setSpecName(specName);
                specification.setStatus(status);
                specificationList.add(specification);
            }
            specificationService.addSpecList(specificationList);
            return "redirect:/admin/specification.html";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/admin/error.html";
        }
    }


    @RequestMapping("readTemplateXls")
    public String readTemplateXls(MultipartFile file){
        try {
            List<TypeTemplate> typeTemplateList=new ArrayList<>();
            InputStream is = file.getInputStream();
            BufferedInputStream bufferedInputStream=new BufferedInputStream(is);
            POIFSFileSystem fileSystem=new POIFSFileSystem(bufferedInputStream);
            HSSFWorkbook book=new HSSFWorkbook(fileSystem);
            HSSFSheet sheet=book.getSheetAt(0);

            for (int i = 1; i < sheet.getLastRowNum()+1; i++) {
                HSSFRow row = sheet.getRow(i);
                row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
                String name = row.getCell(0).getStringCellValue();
                String specIds = row.getCell(1).getStringCellValue();
                String brandIds= row.getCell(2).getStringCellValue();
                String customAttributeItems= row.getCell(3).getStringCellValue();
                String status = row.getCell(4).getStringCellValue();
                TypeTemplate typeTemplate=new TypeTemplate();
                typeTemplate.setName(name);
                typeTemplate.setSpecIds(specIds);
                typeTemplate.setBrandIds(brandIds);
                typeTemplate.setCustomAttributeItems(customAttributeItems);
                typeTemplate.setStatus(status);
                typeTemplateList.add(typeTemplate);
            }
            if (typeTemplateList.size()>0){
                for (TypeTemplate typeTemplate : typeTemplateList) {
                    typeTemplateService.add(typeTemplate);
                }
            }
            return "redirect:/admin/type_template.html";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/admin/error.html";
        }
    }


    @RequestMapping("readItemCatXls")
    public String readItemCatXls(MultipartFile file){
        try {
            List<ItemCat> itemCatList=new ArrayList<>();
            InputStream is = file.getInputStream();
            BufferedInputStream bufferedInputStream=new BufferedInputStream(is);
            POIFSFileSystem fileSystem=new POIFSFileSystem(bufferedInputStream);
            HSSFWorkbook book=new HSSFWorkbook(fileSystem);
            HSSFSheet sheet=book.getSheetAt(0);

            for (int i = 1; i < sheet.getLastRowNum()+1; i++) {
                HSSFRow row = sheet.getRow(i);
                row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
                String parentId = row.getCell(0).getStringCellValue();
                String name = row.getCell(1).getStringCellValue();
                String typeId= row.getCell(2).getStringCellValue();
                String status = row.getCell(3).getStringCellValue();
                ItemCat itemCat=new ItemCat();
                itemCat.setParentId(Long.parseLong(parentId));
                itemCat.setName(name);
                itemCat.setTypeId(Long.parseLong(typeId));
                itemCat.setStatus(status);
                itemCatList.add(itemCat);
            }
            if (itemCatList.size()>0){
                for (ItemCat itemCat : itemCatList) {
                    itemCatService.add(itemCat);
                }
            }
            return "redirect:/admin/item_cat.html";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/admin/error.html";
        }
    }
}
