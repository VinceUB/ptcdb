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
                """.formatted(req.getRequestURL().append("/images/banner.jpeg")));

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
