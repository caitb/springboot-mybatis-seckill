package com.caitb.seckill.web;

import com.caitb.seckill.dto.Exposer;
import com.caitb.seckill.dto.SeckillExecution;
import com.caitb.seckill.dto.SeckillResult;
import com.caitb.seckill.entity.Seckill;
import com.caitb.seckill.enums.SeckillStatEnum;
import com.caitb.seckill.exception.RepeatKillException;
import com.caitb.seckill.exception.SeckillCloseException;
import com.caitb.seckill.service.Seckillervice;
import com.caitb.seckill.service.SuccessKilledService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @作者 cai_tb
 * @创建时间 2018/7/13
 */
@Controller
@RequestMapping("/seckill")
@Slf4j
public class SeckillController {

    @Autowired
    private Seckillervice seckillervice;
    @Autowired
    private SuccessKilledService successKilledService;

    @GetMapping("")
    public String list(Model model) {
        List<Seckill> seckills = seckillervice.listSeckill();
        model.addAttribute("seckills", seckills);
        return "seckill/list";
    }

    @GetMapping("/{seckillId}/detail")
    public String detail(@PathVariable(name = "seckillId")Long seckillId, Model model) {
        if (seckillId == null) {
            return "forward:seckill/list";
        }
        Seckill seckill = seckillervice.getSeckill(seckillId);
        if (seckill == null) {
            return "forward:seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "seckill/detail";
    }

    /**
     * 暴露秒杀接口的方法.
     *
     * @param seckillId 秒杀商品的id
     * @return 根据用户秒杀的商品id进行业务逻辑判断，返回不同的json实体结果
     */
    @GetMapping("/{seckillId}/exposer")
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable(name = "seckillId")Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillervice.exportSeckillUrl(seckillId);
            result = new SeckillResult<>(true, exposer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = new SeckillResult<>(false, e.getMessage());
        }
        return result;
    }

    /**
     * 用户执行秒杀,在页面点击相应的秒杀连接，进入后获取对应的参数进行判断,返回相对应的json实体结果,前端再进行处理.
     *
     * @param seckillId 秒杀的商品，对应的时秒杀的id
     * @param md5       一个被混淆的md5加密值
     * @param userPhone 参与秒杀用户的额手机号码，当做账号密码使用
     * @return 参与秒杀的结果，为json数据
     */
    @PostMapping("/{seckillId}/{md5}/execution")
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable(name ="seckillId")Long seckillId,
                                                   @PathVariable(name = "md5")String md5,
                                                   //@CookieValue(value = "userPhone", required = false)
                                                               Long userPhone) {
        if (userPhone == null) {
            return new SeckillResult<SeckillExecution>(false, "未注册");
        }
        SeckillResult<SeckillExecution> result;
        try {
            SeckillExecution execution = seckillervice.executeSeckill(seckillId, userPhone, md5);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (RepeatKillException e) {
            // 重复秒杀
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(false, execution);
        } catch (SeckillCloseException e) {
            // 秒杀关闭
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<SeckillExecution>(false, execution);
        } catch (Exception e) {
            // 不能判断的异常
            log.error(e.getMessage(), e);
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(false, execution);
        }
    }

    /**
     * 获取服务器端时间,防止用户篡改客户端时间提前参与秒杀
     *
     * @return 时间的json数据
     */
    @GetMapping("/time/now")
    @ResponseBody
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<Long>(true, now.getTime());
    }

}
