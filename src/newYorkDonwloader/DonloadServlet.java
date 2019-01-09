package newYorkDonwloader;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DonloadServlet
 */
@WebServlet("/DonloadServlet")
public class DonloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DonloadServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String[] input = request.getParameterValues("Array1[]");
		String Filebase = request.getParameter("Filebase");
		String urlbase = request.getParameter("urlbase");
		String token = request.getParameter("token");
		String prefix = request.getParameter("prefix");
		String needTokan = request.getParameter("needTokan");
		for (int i = 0; i < input.length; i++) {
			String path = prefix + input[i];
			String folder = path.substring(0, path.lastIndexOf("/") + 1);
			File tmp = new File(Filebase + folder);
			if (!tmp.exists()) {
				tmp.mkdirs();
			}
			File b = new File(Filebase + path);
			if (!b.exists()) {
				b.createNewFile();
			}
			if (b.length() > 0)
				continue;
			Detect401 as = null;
			if (needTokan.equals("1")) {
				as = HttpRequestUtil.postDownTerrain(urlbase + path + token, b);
			} else if (needTokan.equals("0")) {
				as = HttpRequestUtil.postDownTerrain(urlbase + path, b);
			}
			if (as == null) {
				response.getWriter().write("500");
				response.getWriter().flush();
				response.getWriter().close();
				b.delete();
				tmp.deleteOnExit();
				return;
			}
			if (as.getCode() == 404) {
				b.deleteOnExit();
				tmp.deleteOnExit();
				continue;
			}
			if (as.getCode() == 500) {
				System.out.println(path + "远端服务器报错500已经跳过");
				b.delete();
				continue;
			}
			if (as.getCode() == 401) {
				response.getWriter().write("401");
				response.getWriter().flush();
				response.getWriter().close();
				b.delete();
				return;
			}
			/*
			 * if (FileUtil.unGzipFile(b, a) == false) { response.getWriter().write("500");
			 * response.getWriter().flush(); response.getWriter().close(); b.delete();
			 * a.delete(); return; }
			 */
		}
		response.getWriter().write("200");
		response.getWriter().flush();
		response.getWriter().close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
