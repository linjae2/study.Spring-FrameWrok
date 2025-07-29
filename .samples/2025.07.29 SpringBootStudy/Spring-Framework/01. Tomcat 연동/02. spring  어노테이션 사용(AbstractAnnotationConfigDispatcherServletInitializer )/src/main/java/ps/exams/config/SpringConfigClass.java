package ps.exams.config;

import jakarta.servlet.Filter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class SpringConfigClass extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    //DispatcherServlet에 매핑할 요청 주소를 세팅한다.
    protected String[] getServletMappings() {
        // TODO Auto-generated method stub
        return new String[] {"/"};
    }

    @Override
    //Spring MVC 프로젝트 설정을 위한 클래스 지정
    protected Class<?>[] getServletConfigClasses() {
        // TODO Auto-generated method stub
        return new Class[] {ServletAppContext.class};
    }

    @Override
    //프로젝트에서 사용할 bean들을 정의하기 위한 클래스 지정
    protected Class<?>[] getRootConfigClasses() {
        // TODO Auto-generated method stub
        return new Class[] {RootAppContext.class};
    }

    @Override
    //파라미터 인코딩 필터 설정
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        return new Filter[] {encodingFilter};
    }
}
