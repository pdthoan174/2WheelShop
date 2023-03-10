/*
 * (C) Copyright 2022. All Rights Reserved.
 *
 * @author DongTHD
 * @date Mar 10, 2022
*/
package vn.fs.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.fs.entity.Order;
import vn.fs.entity.OrderDetail;
import vn.fs.repository.OrderDetailRepository;
import vn.fs.repository.OrderRepository;
import vn.fs.service.SendMailService;

@Service
public class SendMailUtil {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Autowired
	SendMailService sendMailService;

	public void sendMailOrder(Order order) {
		SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
		List<OrderDetail> listOrderDetails = orderDetailRepository.findByOrder(order);
		StringBuilder content = new StringBuilder();
		content.append(HEADER);
		for (OrderDetail oderDetail : listOrderDetails) {
			content.append("<tr>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;text-align: center;\">\r\n"
					+ "                                                        <img style=\"width: 85%;\" src="
					+ oderDetail.getProduct().getImage() + ">\r\n"
					+ "                                                    </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ oderDetail.getProduct().getName() + " </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ oderDetail.getQuantity() + " </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ format(String.valueOf(oderDetail.getPrice())) + " </td>\r\n"
					+ "                                                </tr>");
		}
		content.append(BODY2);
		content.append(
				"<td width=\"55%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee;\"> T???ng ti???n: </td>\r\n"
						+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee; color: red;\"> "
						+ format(String.valueOf(order.getAmount())) + " </td>");
		content.append(BODY3);
		content.append(
				"<td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">?????a ch??? giao h??ng</p>\r\n"
						+ "                                                            <p>" + order.getAddress()
						+ "</p>\r\n" + "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Ng??y ?????t h??ng</p>\r\n"
						+ "                                                            <p>"
						+ dt.format(order.getOrderDate()) + "</p>\r\n"
						+ "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">T??n ng?????i nh???n</p>\r\n"
						+ "                                                            <p>" + order.getUser().getName()
						+ "</p>\r\n" + "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">S??? ??i???n tho???i</p>\r\n"
						+ "                                                            <p>" + order.getPhone()
						+ "</p>\r\n" + "                                                        </td>");
		content.append(FOOTER);
		sendMailService.queue(order.getUser().getEmail(), "?????t h??ng th??nh c??ng", content.toString());
	}

	public void sendMailOrderSuccess(Order order) {
		SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
		List<OrderDetail> listOrderDetails = orderDetailRepository.findByOrder(order);
		StringBuilder content = new StringBuilder();
		content.append(HEADERSUCCESS);
		for (OrderDetail oderDetail : listOrderDetails) {
			content.append("<tr>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;text-align: center;\">\r\n"
					+ "                                                        <img style=\"width: 85%;\" src="
					+ oderDetail.getProduct().getImage() + ">\r\n"
					+ "                                                    </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ oderDetail.getProduct().getName() + " </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ oderDetail.getQuantity() + " </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ format(String.valueOf(oderDetail.getPrice())) + " </td>\r\n"
					+ "                                                </tr>");
		}
		content.append(BODY2);
		content.append(
				"<td width=\"55%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee;\"> T???ng ti???n: </td>\r\n"
						+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee; color: red;\"> "
						+ format(String.valueOf(order.getAmount())) + " </td>");
		content.append(BODY3);
		content.append(
				"<td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">?????a ch??? giao h??ng</p>\r\n"
						+ "                                                            <p>" + order.getAddress()
						+ "</p>\r\n" + "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Ng??y ?????t h??ng</p>\r\n"
						+ "                                                            <p>"
						+ dt.format(order.getOrderDate()) + "</p>\r\n"
						+ "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">T??n ng?????i nh???n</p>\r\n"
						+ "                                                            <p>" + order.getUser().getName()
						+ "</p>\r\n" + "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">S??? ??i???n tho???i</p>\r\n"
						+ "                                                            <p>" + order.getPhone()
						+ "</p>\r\n" + "                                                        </td>");
		content.append(FOOTER);
		sendMailService.queue(order.getUser().getEmail(), "Thanh to??n th??nh c??ng", content.toString());
	}

	public void sendMailOrderDeliver(Order order) {
		SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
		List<OrderDetail> listOrderDetails = orderDetailRepository.findByOrder(order);
		StringBuilder content = new StringBuilder();
		content.append(HEADERDELIVER);
		for (OrderDetail oderDetail : listOrderDetails) {
			content.append("<tr>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;text-align: center;\">\r\n"
					+ "                                                        <img style=\"width: 85%;\" src="
					+ oderDetail.getProduct().getImage() + ">\r\n"
					+ "                                                    </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ oderDetail.getProduct().getName() + " </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ oderDetail.getQuantity() + " </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ format(String.valueOf(oderDetail.getPrice())) + " </td>\r\n"
					+ "                                                </tr>");
		}
		content.append(BODY2);
		content.append(
				"<td width=\"55%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee;\"> T???ng ti???n: </td>\r\n"
						+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee; color: red;\"> "
						+ format(String.valueOf(order.getAmount())) + " </td>");
		content.append(BODY3);
		content.append(
				"<td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">?????a ch??? giao h??ng</p>\r\n"
						+ "                                                            <p>" + order.getAddress()
						+ "</p>\r\n" + "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Ng??y ?????t h??ng</p>\r\n"
						+ "                                                            <p>"
						+ dt.format(order.getOrderDate()) + "</p>\r\n"
						+ "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">T??n ng?????i nh???n</p>\r\n"
						+ "                                                            <p>" + order.getUser().getName()
						+ "</p>\r\n" + "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">S??? ??i???n tho???i</p>\r\n"
						+ "                                                            <p>" + order.getPhone()
						+ "</p>\r\n" + "                                                        </td>");
		content.append(FOOTER);
		sendMailService.queue(order.getUser().getEmail(), "????n h??ng ???? ???????c x??c nh???n", content.toString());
	}

	public void sendMailOrderCancel(Order order) {
		SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
		List<OrderDetail> listOrderDetails = orderDetailRepository.findByOrder(order);
		StringBuilder content = new StringBuilder();
		content.append(HEADERCANCEL);
		for (OrderDetail oderDetail : listOrderDetails) {
			content.append("<tr>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;text-align: center;\">\r\n"
					+ "                                                        <img style=\"width: 85%;\" src="
					+ oderDetail.getProduct().getImage() + ">\r\n"
					+ "                                                    </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ oderDetail.getProduct().getName() + " </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ oderDetail.getQuantity() + " </td>\r\n"
					+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;\"> "
					+ format(String.valueOf(oderDetail.getPrice())) + " </td>\r\n"
					+ "                                                </tr>");
		}
		content.append(BODY2);
		content.append(
				"<td width=\"55%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee;\"> T???ng ti???n: </td>\r\n"
						+ "                                                    <td width=\"25%\" align=\"left\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee; color: red;\"> "
						+ format(String.valueOf(order.getAmount())) + " </td>");
		content.append(BODY3);
		content.append(
				"<td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">?????a ch??? giao h??ng</p>\r\n"
						+ "                                                            <p>" + order.getAddress()
						+ "</p>\r\n" + "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">Ng??y ?????t h??ng</p>\r\n"
						+ "                                                            <p>"
						+ dt.format(order.getOrderDate()) + "</p>\r\n"
						+ "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">T??n ng?????i nh???n</p>\r\n"
						+ "                                                            <p>" + order.getUser().getName()
						+ "</p>\r\n" + "                                                        </td>\r\n"
						+ "                                                    </tr>\r\n"
						+ "                                                </table>\r\n"
						+ "                                            </div>\r\n"
						+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
						+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
						+ "                                                    <tr>\r\n"
						+ "                                                        <td align=\"center\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">\r\n"
						+ "                                                            <p style=\"font-weight: 800;\">S??? ??i???n tho???i</p>\r\n"
						+ "                                                            <p>" + order.getPhone()
						+ "</p>\r\n" + "                                                        </td>");
		content.append(FOOTER);
		sendMailService.queue(order.getUser().getEmail(), "Hu??? ????n th??nh c??ng", content.toString());
	}

	public String format(String number) {
		DecimalFormat formatter = new DecimalFormat("###,###,###.##");

		return formatter.format(Double.valueOf(number)) + " VN??";
	}

	static String HEADERDELIVER = "<body style=\"margin: 0 !important; padding: 0 !important; background-color: #fcb800;\" bgcolor=\"#fcb800\">\r\n"
			+ "    <div style=\"display: none; font-size: 1px; color: #fefefe; line-height: 1px; font-family: Open Sans, Helvetica, Arial, sans-serif; max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden;\">\r\n"
			+ "        Vegana Store chuy??n b??n th???c ph???m s???ch\r\n" + "    </div>\r\n"
			+ "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + "        <tr>\r\n"
			+ "            <td align=\"center\" style=\"background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\r\n"
			+ "                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\r\n"
			+ "                    <tr>\r\n"
			+ "                        <td align=\"center\" valign=\"top\" style=\"font-size:0; padding: 35px;\" bgcolor=\"#fcb800\">\r\n"
			+ "                            <div style=\"display:inline-block; max-width:50%; min-width:100px; vertical-align:top; width:100%;\">\r\n"
			+ "                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
			+ "                                    <tr>\r\n"
			+ "                                        <td align=\"left\" valign=\"top\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 36px; font-weight: 800; line-height: 48px;\" class=\"mobile-center\">\r\n"
			+ "                                            <img src=\"https://res.cloudinary.com/dtf4jsbwe/image/upload/v1671347599/logo_shop_fbnvzu.png\" width=\"220px\"/>\r\n"
			+ "                                        </td>\r\n" + "                                    </tr>\r\n"
			+ "                                </table>\r\n" + "                            </div>\r\n"
			+ "                            \r\n" + "                        </td>\r\n" + "                    </tr>\r\n"
			+ "                    <tr>\r\n"
			+ "                        <td align=\"center\" style=\"padding: 35px 35px 20px 35px; background-color: #ffffff;\" bgcolor=\"#ffffff\">\r\n"
			+ "                            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\r\n"
			+ "                                <tr>\r\n"
			+ "                                    <td align=\"center\" style=\"font-family: Open sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding-top: 25px;\">\r\n"
			+ "                                        <img src=\"https://res.cloudinary.com/veggie-shop/image/upload/v1634045009/assets/checked_pudgic.png?fbclid=IwAR2aTBpMU1Gbj8pVwuU6sH1lUAUEeK2U8df1mrI4zCyMT97OnjkEIbgBSQw\" width=\"115\" height=\"110\" style=\"display: block; border: 0px;\" /><br>\r\n"
			+ "                                        <h2 style=\"font-size: 30px; font-weight: 800; line-height: 36px; color: #333333; margin: 0;\"> Giao h??ng th??nh c??ng! </h2>\r\n"
			+ "                                        <p style=\"font-family: Open sans-serif; font-size: 18px;\"><em>C???m ??n b???n ???? tin t?????ng! Ch??c b???n m???t ng??y vui v???!</em></p>\r\n"
			+ "                                        <p style=\"font-family: Open sans-serif; font-size: 18px;\"><em>H???n g???p l???i qu?? kh??ch!</em></p>\r\n"
			+ "                                    </td>\r\n" + "                                </tr>\r\n"
			+ "                                \r\n" + "                                <tr>\r\n"
			+ "                                    <td align=\"left\" style=\"padding-top: 20px;\">\r\n"
			+ "                                        <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\r\n"
			+ "                                            <p style=\"font-size: 20px;font-family: Open sans-serif; text-decoration: underline; width: 200px;\">????n h??ng ???? giao:</p>\r\n"
			+ "                                            <tr>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">#</td>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">T??n</td>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">S??? l?????ng</td>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">Th??nh ti???n</td>\r\n"
			+ "                                            </tr>";

	static String HEADERSUCCESS = "<body style=\"margin: 0 !important; padding: 0 !important; background-color: #fcb800;\" bgcolor=\"#fcb800\">\r\n"
			+ "    <div style=\"display: none; font-size: 1px; color: #fefefe; line-height: 1px; font-family: Open Sans, Helvetica, Arial, sans-serif; max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden;\">\r\n"
			+ "        Vegana Store chuy??n b??n th???c ph???m s???ch\r\n" + "    </div>\r\n"
			+ "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + "        <tr>\r\n"
			+ "            <td align=\"center\" style=\"background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\r\n"
			+ "                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\r\n"
			+ "                    <tr>\r\n"
			+ "                        <td align=\"center\" valign=\"top\" style=\"font-size:0; padding: 35px;\" bgcolor=\"#fcb800\">\r\n"
			+ "                            <div style=\"display:inline-block; max-width:50%; min-width:100px; vertical-align:top; width:100%;\">\r\n"
			+ "                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
			+ "                                    <tr>\r\n"
			+ "                                        <td align=\"left\" valign=\"top\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 36px; font-weight: 800; line-height: 48px;\" class=\"mobile-center\">\r\n"
			+ "                                            <img src=\"https://res.cloudinary.com/dtf4jsbwe/image/upload/v1671347599/logo_shop_fbnvzu.png\" width=\"220px\"/>\r\n"
			+ "                                        </td>\r\n" + "                                    </tr>\r\n"
			+ "                                </table>\r\n" + "                            </div>\r\n"
			+ "                            \r\n" + "                        </td>\r\n" + "                    </tr>\r\n"
			+ "                    <tr>\r\n"
			+ "                        <td align=\"center\" style=\"padding: 35px 35px 20px 35px; background-color: #ffffff;\" bgcolor=\"#ffffff\">\r\n"
			+ "                            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\r\n"
			+ "                                <tr>\r\n"
			+ "                                    <td align=\"center\" style=\"font-family: Open sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding-top: 25px;\">\r\n"
			+ "                                        <img src=\"https://res.cloudinary.com/veggie-shop/image/upload/v1634045009/assets/checked_pudgic.png?fbclid=IwAR2aTBpMU1Gbj8pVwuU6sH1lUAUEeK2U8df1mrI4zCyMT97OnjkEIbgBSQw\" width=\"115\" height=\"110\" style=\"display: block; border: 0px;\" /><br>\r\n"
			+ "                                        <h2 style=\"font-size: 30px; font-weight: 800; line-height: 36px; color: #333333; margin: 0;\"> X??c nh???n ???? thanh to??n th??nh c??ng! </h2>\r\n"
			+ "                                        <p style=\"font-family: Open sans-serif; font-size: 18px;\"><em>C???m ??n b???n ???? tin t?????ng! Ch??ng t??i s??? nhanh ch??ng giao h??ng cho b???n!</em></p>\r\n"
			+ "                                    </td>\r\n" + "                                </tr>\r\n"
			+ "                                \r\n" + "                                <tr>\r\n"
			+ "                                    <td align=\"left\" style=\"padding-top: 20px;\">\r\n"
			+ "                                        <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\r\n"
			+ "                                            <p style=\"font-size: 20px;font-family: Open sans-serif; text-decoration: underline; width: 200px;\">????n h??ng ???? thanh to??n:</p>\r\n"
			+ "                                            <tr>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">#</td>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">T??n</td>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">S??? l?????ng</td>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">Th??nh ti???n</td>\r\n"
			+ "                                            </tr>";

	static String HEADERCANCEL = "<body style=\"margin: 0 !important; padding: 0 !important; background-color: #fcb800;\" bgcolor=\"#fcb800\">\r\n"
			+ "    <div style=\"display: none; font-size: 1px; color: #fefefe; line-height: 1px; font-family: Open Sans, Helvetica, Arial, sans-serif; max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden;\">\r\n"
			+ "        Vegana Store chuy??n b??n th???c ph???m s???ch\r\n" + "    </div>\r\n"
			+ "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + "        <tr>\r\n"
			+ "            <td align=\"center\" style=\"background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\r\n"
			+ "                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\r\n"
			+ "                    <tr>\r\n"
			+ "                        <td align=\"center\" valign=\"top\" style=\"font-size:0; padding: 35px;\" bgcolor=\"#fcb800\">\r\n"
			+ "                            <div style=\"display:inline-block; max-width:50%; min-width:100px; vertical-align:top; width:100%;\">\r\n"
			+ "                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
			+ "                                    <tr>\r\n"
			+ "                                        <td align=\"left\" valign=\"top\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 36px; font-weight: 800; line-height: 48px;\" class=\"mobile-center\">\r\n"
			+ "                                            <img src=\"https://res.cloudinary.com/dtf4jsbwe/image/upload/v1671347599/logo_shop_fbnvzu.png\" width=\"220px\"/>\r\n"
			+ "                                        </td>\r\n" + "                                    </tr>\r\n"
			+ "                                </table>\r\n" + "                            </div>\r\n"
			+ "                            \r\n" + "                        </td>\r\n" + "                    </tr>\r\n"
			+ "                    <tr>\r\n"
			+ "                        <td align=\"center\" style=\"padding: 35px 35px 20px 35px; background-color: #ffffff;\" bgcolor=\"#ffffff\">\r\n"
			+ "                            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\r\n"
			+ "                                <tr>\r\n"
			+ "                                    <td align=\"center\" style=\"font-family: Open sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding-top: 25px;\">\r\n"
			+ "                                        <img src=\"https://res.cloudinary.com/veggie-shop/image/upload/v1634046654/assets/cancellation_xhljqh.png\" width=\"115\" height=\"110\" style=\"display: block; border: 0px;\" /><br>\r\n"
			+ "                                        <h2 style=\"font-family: Open sans-serif;font-size: 30px; font-weight: 800; line-height: 36px; color: #333333; margin: 0;\"> H???y ????n h??ng th??nh c??ng! </h2>\r\n"
			+ "                                        <p style=\"font-family: Open sans-serif; font-size: 18px;\"><em>Ch??ng t??i r???t ti???c v??? v???n ????? n??y, h???n g???p l???i qu?? kh??ch!</em></p>\r\n"
			+ "                                    </td>\r\n" + "                                </tr>\r\n"
			+ "                                \r\n" + "                                <tr>\r\n"
			+ "                                    <td align=\"left\" style=\"padding-top: 20px;\">\r\n"
			+ "                                        <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\r\n"
			+ "                                            <p style=\"font-size: 20px;font-family: Open sans-serif; text-decoration: underline; width: 200px;\">????n h??ng ???? h???y:</p>\r\n"
			+ "                                            <tr>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">#</td>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">T??n</td>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">S??? l?????ng</td>\r\n"
			+ "                                                <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">Th??nh ti???n</td>\r\n"
			+ "                                            </tr>";

	static String HEADER = "<body style=\"margin: 0 !important; padding: 0 !important; background-color: #fcb800;\" bgcolor=\"#fcb800\">\r\n"
			+ "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n"
			+ "            <tr>\r\n"
			+ "                <td align=\"center\" style=\"background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\r\n"
			+ "                    <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\r\n"
			+ "                        <tr>\r\n"
			+ "                            <td align=\"center\" valign=\"top\" style=\"font-size:0; padding: 35px;\" bgcolor=\"#fcb800\">\r\n"
			+ "                                <div style=\"display:inline-block; max-width:50%; min-width:100px; vertical-align:top; width:100%;\">\r\n"
			+ "                                    <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
			+ "                                        <tr>\r\n"
			+ "                                            <td align=\"left\" valign=\"top\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 36px; font-weight: 800; line-height: 48px;\" class=\"mobile-center\">\r\n"
			+ "                                                <img src=\"https://res.cloudinary.com/dtf4jsbwe/image/upload/v1671347599/logo_shop_fbnvzu.png\" width=\"220px\" />\r\n"
			+ "                                            </td>\r\n"
			+ "                                        </tr>\r\n" + "                                    </table>\r\n"
			+ "                                </div>\r\n" + "\r\n" + "                            </td>\r\n"
			+ "                        </tr>\r\n" + "                        <tr>\r\n"
			+ "                            <td align=\"center\" style=\"padding: 35px 35px 20px 35px; background-color: #ffffff;\" bgcolor=\"#ffffff\">\r\n"
			+ "                                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\r\n"
			+ "                                    <tr>\r\n"
			+ "                                        <td align=\"center\" style=\"font-family: Open sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding-top: 25px;\">\r\n"
			+ "                                            <img src=\"https://res.cloudinary.com/veggie-shop/image/upload/v1634045009/assets/checked_pudgic.png?fbclid=IwAR2aTBpMU1Gbj8pVwuU6sH1lUAUEeK2U8df1mrI4zCyMT97OnjkEIbgBSQw\" width=\"115\" height=\"110\" style=\"display: block; border: 0px;\" /><br>\r\n"
			+ "                                            <h2 style=\"font-size: 30px; font-weight: 800; line-height: 36px; color: #333333; margin: 0;\"> Ch??c m???ng ???? ?????t h??ng th??nh c??ng! </h2>\r\n"
			+ "                                            <p style=\"font-family: Open sans-serif; font-size: 18px;\"><em>Ch??ng t??i s??? ti???n h??nh x??? l?? ????n h??ng s???m nh???t, b???n vui l??ng ch??? nh??!</em></p>\r\n"
			+ "                                        </td>\r\n" + "                                    </tr>\r\n"
			+ "\r\n" + "                                    <tr>\r\n"
			+ "                                        <td align=\"left\" style=\"padding-top: 20px;\">\r\n"
			+ "                                            <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\r\n"
			+ "                                                <p style=\"font-size: 20px;font-family: Open sans-serif; text-decoration: underline; width: 200px;\">????n h??ng c???a b???n:</p>\r\n"
			+ "                                                <tr>\r\n"
			+ "                                                    <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">#</td>\r\n"
			+ "                                                    <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">T??n</td>\r\n"
			+ "                                                    <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">S??? l?????ng</td>\r\n"
			+ "                                                    <td width=\"25%\" align=\"left\" bgcolor=\"#eeeeee\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 800; line-height: 24px; padding: 10px;\">Th??nh ti???n</td>\r\n"
			+ "                                                </tr>";
	static String BODY2 = "</table>\r\n" + "                                        </td>\r\n"
			+ "                                    </tr>\r\n" + "                                    <tr>\r\n"
			+ "                                        <td align=\"left\" style=\"padding-top: 20px;\">\r\n"
			+ "                                            <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\r\n"
			+ "                                                <tr>";
	static String BODY3 = "</tr>\r\n" + "                                            </table>\r\n"
			+ "                                        </td>\r\n" + "                                    </tr>\r\n"
			+ "                                </table>\r\n" + "                            </td>\r\n"
			+ "                        </tr>\r\n" + "                        <tr>\r\n"
			+ "                            <td align=\"center\" height=\"100%\" valign=\"top\" width=\"100%\" style=\"padding: 0 35px 35px 35px; background-color: #ffffff;\" bgcolor=\"#ffffff\">\r\n"
			+ "                                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:660px;\">\r\n"
			+ "                                    <tr>\r\n"
			+ "                                        <td align=\"center\" valign=\"top\" style=\"font-size:0;\">\r\n"
			+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
			+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
			+ "                                                    <tr>\r\n"
			+ "                                                        <td align=\"left\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">";
	static String BODY4 = "</td>\r\n" + "                                                    </tr>\r\n"
			+ "                                                </table>\r\n"
			+ "                                            </div>\r\n"
			+ "                                            <div style=\"display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;\">\r\n"
			+ "                                                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\r\n"
			+ "                                                    <tr>\r\n"
			+ "                                                        <td align=\"left\" valign=\"top\" style=\"font-family: Open sans-serif; font-size: 20px; font-weight: 400; line-height: 24px;\">";
	static String FOOTER = "</tr>\r\n" + "                                                </table>\r\n"
			+ "                                            </div>\r\n"
			+ "                                        </td>\r\n" + "                                    </tr>\r\n"
			+ "                                </table>\r\n" + "                            </td>\r\n"
			+ "                        </tr>\r\n" + "\r\n" + "                        <tr>\r\n"
			+ "                            <td align=\"center\" style=\"padding: 35px; background-color: #ffffff;\" bgcolor=\"#ffffff\">\r\n"
			+ "                                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\r\n"
			+ "                                    <tr>\r\n"
			+ "                                        <td align=\"center\" style=\"font-family: Open sans-serif; font-size: 18px; font-weight: 400; line-height: 24px; padding: 5px 0 10px 0;\">\r\n"
			+ "                                            <p style=\"font-size: 18px; font-weight: 800; line-height: 18px; color: #fcb800;\"> Shop2Wheel </p>\r\n"
			+ "                                            <p style=\"font-family: Open sans-serif;\">C???m ??n b???n ???? tin t?????ng ch??ng t??i - Ch??c b???n m???t ng??y vui v???!</p>\r\n"
			+ "                                        </td>\r\n" + "                                    </tr>\r\n"
			+ "\r\n" + "                                </table>\r\n" + "                            </td>\r\n"
			+ "                        </tr>\r\n" + "                    </table>\r\n" + "                </td>\r\n"
			+ "            </tr>\r\n" + "        </table>\r\n" + "    </body>";
}
