Create database pi_db;

use pi_db;

Select * from cenovnik;

Select * from faktura;

Select * from mesto;

Select * from jedinica_mere;

Select * from poslovni_partner;

Select * from poslovna_godina;

Select * from preduzece;

Select * from roba;

Select * from grupa_robe;

Select * from pdv;

Select * from stopa_pdv;

Select * from stavka_cenovnika;

Select * from stavka_fakture;

--------------------------------------------------------
use pi_db;
INSERT INTO mesto (mesto_id,drzava,grad) VALUES (1,'Srbija','Beograd');
INSERT INTO mesto (mesto_id,drzava,grad) VALUES (2,'Srbija','Novi Sad');
INSERT INTO mesto (mesto_id,drzava,grad) VALUES (3,'Srbija','Kragujevac');
INSERT INTO mesto (mesto_id,drzava,grad) VALUES (4,'Srbija','Loznica');
INSERT INTO mesto (mesto_id,drzava,grad) VALUES (5,'Srbija','Ruma');

INSERT INTO jedinica_mere (jedinica_mere_id,naziv) VALUES (1,'m');
INSERT INTO jedinica_mere (jedinica_mere_id,naziv) VALUES (2,'cm');
INSERT INTO jedinica_mere (jedinica_mere_id,naziv) VALUES (3,'mm');
INSERT INTO jedinica_mere (jedinica_mere_id,naziv) VALUES (4,'kg');

INSERT INTO poslovna_godina (poslovna_godina_id,godina,zakljucena) VALUES (1,2017,true);
INSERT INTO poslovna_godina (poslovna_godina_id,godina,zakljucena) VALUES (2,2016,true);
INSERT INTO poslovna_godina (poslovna_godina_id,godina,zakljucena) VALUES (3,2015,true);
INSERT INTO poslovna_godina (poslovna_godina_id,godina,zakljucena) VALUES (4,2014,true);
INSERT INTO poslovna_godina (poslovna_godina_id,godina,zakljucena) VALUES (5,2013,true);

INSERT INTO preduzece (preduzece_id,adresa,email,logo_path,naziv,pib,telefon,mesto_id) VALUES (1,'Alekse Santica 8','preduzece1@gmail.com','logo1','Preduzece 1',1,'021478325',2);
INSERT INTO preduzece (preduzece_id,adresa,email,logo_path,naziv,pib,telefon,mesto_id) VALUES (2,'Milana Dakica 17','preduzece2@gmail.com','logo2','Preduzece 2',2,'018485236',3);
INSERT INTO preduzece (preduzece_id,adresa,email,logo_path,naziv,pib,telefon,mesto_id) VALUES (3,'Knez Mihajlova','preduzece3@gmail.com','logo3','Preduzece 3',3,'011485966',1);


INSERT INTO pdv (pdv_id,naziv) VALUES (1,'mesecni');
INSERT INTO pdv (pdv_id,naziv) VALUES (2,'tromesecni');

INSERT INTO grupa_robe (grupa_robe_id,naziv,pdv_id,preduzece_id) VALUES (1,'Grupa1',1,1);
INSERT INTO grupa_robe (grupa_robe_id,naziv,pdv_id,preduzece_id) VALUES (2,'Grupa2',2,2);
INSERT INTO grupa_robe (grupa_robe_id,naziv,pdv_id,preduzece_id) VALUES (3,'Grupa3',1,3);


INSERT INTO roba (roba_id,naziv,grupa_robe_id,jedinica_mere_id) VALUES (1,'Pamuk',1,4);
INSERT INTO roba (roba_id,naziv,grupa_robe_id,jedinica_mere_id) VALUES (2,'Gvozdje',2,4);
INSERT INTO roba (roba_id,naziv,grupa_robe_id,jedinica_mere_id) VALUES (3,'Svila',1,4);
INSERT INTO roba (roba_id,naziv,grupa_robe_id,jedinica_mere_id) VALUES (4,'Koza',1,4);
INSERT INTO roba (roba_id,naziv,grupa_robe_id,jedinica_mere_id) VALUES (5,'Aluminijum',2,4);

INSERT INTO stopa_pdv (stopa_pdv_id,datum_vazenja,procenat,pdv_id) VALUES (1,'01-01-2018',10,1);
INSERT INTO stopa_pdv (stopa_pdv_id,datum_vazenja,procenat,pdv_id) VALUES (2,'12-12-2012',20,1);



INSERT INTO poslovni_partner (poslovni_partner_id,adresa,naziv,vrsta,mesto_id,preduzece_id) VALUES (1,'Puskinova 17','GMT Company','vrsta1',1,1);
INSERT INTO poslovni_partner (poslovni_partner_id,adresa,naziv,vrsta,mesto_id,preduzece_id) VALUES (2,'Njegoseva 159','Matox','vrsta2',2,2);
INSERT INTO poslovni_partner (poslovni_partner_id,adresa,naziv,vrsta,mesto_id,preduzece_id) VALUES (3,'Lasle Gala','Valmex','vrsta3',3,3);

INSERT INTO cenovnik (cenovnik_id, datum_vazenja, preduzece_id) VALUES (1,'07-07-2017',1);
INSERT INTO cenovnik (cenovnik_id, datum_vazenja, preduzece_id) VALUES (2,'05-10-2015',2);
INSERT INTO cenovnik (cenovnik_id, datum_vazenja, preduzece_id) VALUES (3,'03-12-2014',3);

INSERT INTO faktura (faktura_id,datum_fakture,datum_valute,iznos_za_placanje,osnovica,ukupanpdv,poslovna_godina_id,poslovni_partner_id,preduzece_id) VALUES (1,'01-01-2018','01-01-2017',0,0,0,1,1,1);
INSERT INTO faktura (faktura_id,datum_fakture,datum_valute,iznos_za_placanje,osnovica,ukupanpdv,poslovna_godina_id,poslovni_partner_id,preduzece_id) VALUES (2,'01-01-2015','01-01-2015',0,0,0,1,1,2);
INSERT INTO faktura (faktura_id,datum_fakture,datum_valute,iznos_za_placanje,osnovica,ukupanpdv,poslovna_godina_id,poslovni_partner_id,preduzece_id) VALUES (3,'01-01-2011','01-01-2011',0,0,0,2,2,3);
INSERT INTO faktura (faktura_id,datum_fakture,datum_valute,iznos_za_placanje,osnovica,ukupanpdv,poslovna_godina_id,poslovni_partner_id,preduzece_id) VALUES (4,'01-01-2013','01-01-2013',0,0,0,1,3,3);

INSERT INTO stavka_cenovnika (stavka_cenovnika_id, cena, cenovnik_id,roba_id) VALUES (1,2700,1,1);
INSERT INTO stavka_cenovnika (stavka_cenovnika_id, cena, cenovnik_id,roba_id) VALUES (2,3800,2,2);
INSERT INTO stavka_cenovnika (stavka_cenovnika_id, cena, cenovnik_id,roba_id) VALUES (3,850,3,3);
INSERT INTO stavka_cenovnika (stavka_cenovnika_id, cena, cenovnik_id,roba_id) VALUES (4,250,1,4);
INSERT INTO stavka_cenovnika (stavka_cenovnika_id, cena, cenovnik_id,roba_id) VALUES (5,850,3,5);








