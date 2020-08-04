package com.udemy.data.jpa.view.csv;

import com.udemy.data.jpa.models.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component("list.csv")
public class ClientCsvView extends AbstractView {

    public ClientCsvView(){
        setContentType("text/csv");
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> map,
                                           HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"clients.csv\"");
        httpServletResponse.setContentType(getContentType());
        Page<Client> clients =  (Page<Client>) map.get("clients");

        ICsvBeanWriter beanWriter = new CsvBeanWriter(httpServletResponse.getWriter(), CsvPreference.STANDARD_PREFERENCE); /*standard_preference para dividir las lineas del csv por saltos*/

        String[] header = {"id", "name", "surname", "email", "createAt"};
        beanWriter.writeHeader(header);

        for (Client client: clients) {
            beanWriter.write(client, header);
        }

        beanWriter.close();
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }
}
