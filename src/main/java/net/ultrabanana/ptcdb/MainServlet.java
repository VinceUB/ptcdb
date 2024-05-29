package net.ultrabanana.ptcdb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import net.ultrabanana.ptcdb.cl.PTCDBCL;

import java.io.*;

@WebServlet(name="MainServlet", value = "/index.html")
public class MainServlet extends HttpServlet {
    File[] configFiles;

    @SneakyThrows
    @Override
    public void init(){
        var webInfDirectory = new File(getServletContext().getResource("/WEB-INF/").toURI());

        var configFiles = webInfDirectory.listFiles(
                (file, s) ->
                        s.endsWith(".pdc") |
                        s.endsWith(".ptcdbcl") |
                        s.endsWith(".ptcdbcf")
        );

        System.out.println(webInfDirectory.getAbsolutePath());

        if(configFiles == null || configFiles.length == 0){
            throw new ServletException("No PTCDB configuration file found");
        }

        this.configFiles = configFiles;
    }

    private String removeFileExt(String fileName){
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //String bannerFullUrl = req.getScheme() + "://" + req.getLocalName() + ":" + req.getServerPort() + req.getContextPath() + "/images/banner.jpeg";

        //HMU if you have a better way to do this
        String bannerFullUrl =
            (
                req.getRequestURL().toString().endsWith("/index.html")
                ? req.getRequestURL().substring(0, req.getRequestURL().length()-"/index.html".length())
                : req.getRequestURL().substring(0, req.getRequestURL().length()-"/".length())
            ) + "/images/banner.jpeg";

        resp.getWriter().print("""
                <!DOCTYPE html>
                <html>
                    <head>
                        <title>PTC Database</title>
                        <link rel="icon" type="image/x-icon" href="favicon.ico">
    
                        <meta name="viewport" content="width=device-width, initial-scale=1">
                        <meta charset="utf-8">
    
                        <meta name="description" content=
                              "An exploration of the various PTC's out there">
                        <meta name="keywords" content="PTC, trading cards">
                        <meta name="author" content="Cool Guy Vincent">
    
                        <meta property="og:image" content="%s">
                        <meta property="twitter:card" content="summary_large_image">
    
                        <link rel="stylesheet" href="style.css">
                    </head>
                """.formatted(bannerFullUrl));
        System.out.println(getServletContext().getResource("/images/banner.jpeg").toString());

        resp.getWriter().write(
                """
                <body>
                    <header>
                        <h1>The PTC Database</h1>
                        <p>An exploration of the various PTC's out there</p>
                    </header>
                """.indent(4));

        for (File config : configFiles){
            resp.getWriter().write(
                    """
                    <section>
                        <h2>%s:</h2>
                    """
                    .indent(8)
                    .formatted(removeFileExt(config.getName())));

            PTCDBCL.makeHtmlTable(
                    new BufferedReader(new FileReader(config)),
                    resp.getWriter(),
                    12);

            resp.getWriter().write("</section>".indent(8));

        }

        resp.getWriter().write(
                """
                    </body>
                </html>
                """);
    }
}
