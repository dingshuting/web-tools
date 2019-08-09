package com.ijs.core.system.control;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import com.ijs.core.base.control.BaseControl;
import com.ijs.core.base.model.ExtraData;
import com.ijs.core.base.model.ExtraDataCols;
import com.ijs.core.base.model.ExtraDataColsAuthorization;
import com.ijs.core.util.PageList;
import com.ijs.core.util.QueryParameters;

@Controller
@RequestMapping("/mdyn/extra_data")
public class ExtraDataControl extends BaseControl{


    /**
     * 分页查询
     * @param pn 分页
     * @param request 查询条件
     * @return 数据集
     */
    @RequestMapping("/list/{pn}")
    public @ResponseBody PageList list(@PathVariable("pn")Integer pn,@ModelAttribute("qp")QueryParameters qp,NativeWebRequest request){
        try {
            return  control.list(pn,"extralData", qp, request);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("列表查询异常",e);
        }
        return null;
    }

    @RequestMapping("/info/{iid}")
    public @ResponseBody ExtraData get(@PathVariable("iid")String iid){
        try {
        	  ExtraData ed=genericServ.get(ExtraData.class, iid);
              ExtraDataCols edc=new ExtraDataCols();
              edc.setExtralDataId(ed.getId());
              List<ExtraDataCols> listExtraDataCols=new ArrayList<ExtraDataCols>();
              listExtraDataCols=genericServ.list(edc);
              
             /* for(int i=0;i<listExtraDataCols.size();i++){
                  ExtraDataColsAuthorization extraDataColsAuthorization=new ExtraDataColsAuthorization();
                  extraDataColsAuthorization.setExtraDataColsId(listExtraDataCols.get(i).getId());
                  listExtraDataCols.get(i).setColsAuthorizations(genericServ.list(extraDataColsAuthorization));
              }*/
              ed.setCols(listExtraDataCols);
            return ed;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("数据表信息查询异常，请坚持相关参数是否正确",e);
        }
        return null;
    }


}
