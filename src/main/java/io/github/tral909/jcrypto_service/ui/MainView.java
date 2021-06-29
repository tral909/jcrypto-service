package io.github.tral909.jcrypto_service.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

@CssImport("./styles/styles.css")
public class MainView extends AppLayout {

	public MainView() {
		H1 logo = new H1("JCrypto Service");
		logo.addClassName("logo");
		Anchor logout = new Anchor("logout", "Log out");

		HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logout);
		header.addClassName("header");
		header.expand(logo);
		header.setDefaultVerticalComponentAlignment(
				FlexComponent.Alignment.CENTER);
		header.setWidth("100%");
		addToNavbar(header);

		RouterLink digestPage = new RouterLink("Digest", DigestView.class);
		digestPage.setHighlightCondition(HighlightConditions.sameLocation());
		addToDrawer(new VerticalLayout(digestPage,
				new RouterLink("Keys", KeysView.class),
				new RouterLink("Signature", CryptoSignatureView.class)));
	}
}