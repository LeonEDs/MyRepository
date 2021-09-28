package com.xad;

import com.xad.utils.valid.field.ParamValid;
import com.xad.utils.valid.field.ValidEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DataX
{
    @ParamValid(inEnum = ValidEnum.ENTER_REASON)
    private String Id;

    @ParamValid(notNull = true, maxValue = 8)
    private int submitUnit;

    @ParamValid(notNull = true, maxLength = 3)
    private List<String> sss;

    @ParamValid(notNull = true, maxLength = 3)
    private double [] nums;

    @ParamValid(inEnum = ValidEnum.ENTER_REASON)
    private String code;

    @ParamValid(pattern = "yyyy-mm-dd HH:mm:ss")
    private Date enterDate;
}
