package newYorkDonwloader;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.PriorityQueue;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DonloadServlet
 */
@WebServlet("/MoveServlet")
public class MoveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MoveServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String src = request.getParameter("src");
		File f = new File(src);
		if (f.length() == 0) {
			File parent = f.getParentFile();
			f.delete();
			if (parent.list() == null)
				parent.deleteOnExit();
		}
		if (f.exists() && f.length() > 0) {
			PriorityQueue<MappedByteBuffer> ns = FileUtil.toBytes(f);
			while (!ns.isEmpty()) {
				MappedByteBuffer a = ns.remove();
				if (!a.isLoaded())
					a.load();
				try {
					response.getOutputStream().write(a.array());
				} catch (UnsupportedOperationException e) {
					try {
						/*PriorityQueue<byte[]> bs = FileUtil.toBytesSafe(f);
						while (!bs.isEmpty()) {
							response.getOutputStream().write(bs.remove());
						}*/
						byte[] tmp = new byte[a.capacity()];
						a.get(tmp);
						response.getOutputStream().write(tmp);
					} catch (Exception e1) {
						e1.printStackTrace();
					} 
				}
			}
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} else {
			response.getWriter().write("");
			response.getWriter().flush();
			response.getWriter().close();
		}
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
