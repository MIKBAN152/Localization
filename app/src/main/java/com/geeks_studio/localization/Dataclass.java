package com.geeks_studio.localization;

import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Classic on 26/9/2016.
 */
public class Dataclass {
    Context context;
    Dataclass(Context context){
        this.context=context;
    }
    String[] farmacias={"FYBECA - Cumbaya","Medicity - Cumbaya", "Fybeca", "Medicity Batan"};
    LatLng[] farmpos = {new LatLng(-0.197171, -78.440306),new LatLng(-0.194162,-78.425594)
            ,new LatLng(-0.129640, -78.492393), new LatLng(-0.164428, -78.470049)};
    String[] farmdir={"Cumbaya, Av. Interoceanica","Av. Pampite, Quito EC170157",
    "Avenida de La Prensa, Quito EC170104", "Av. Gral. Eloy Alfaro, Quito"};
    Integer[] farmfoto={R.drawable.farmacia1,R.drawable.farmacia2,
            R.drawable.farmacia3,R.drawable.farmacia4};
    String[] farmhorario={"AutoFarmacia 24 horas","Lunes a Domingo 9am - 8pm",
    "Lunes - Viernes 24 horas", "Lunes - Viernes 8h - 20h"};

    String[] tiendas={"SUPERMAXI", "TIA", "Cupcake Factory", "Panificadora Ambato"};
    LatLng[] tiendapos = {new LatLng(-0.198175,-78.438771),new LatLng(-0.201528,-78.432033),
    new LatLng(-0.187664, -78.481964), new LatLng(-0.169340, -78.473981)};
    String[] tiendadir={"Vía Interoceánica s/n (C.C. Villas de Cumbayá), EC170157","Vía Interoceánica Cumbayá entre Chimborazo y, Salinas",
    "Av. de los Shyris N32-67 y Antonio Navarro, (entre Av. Eloy Alfaro y Av. Diego de Almagro), Quito EC170516",
    "Av. Gaspar de Villarroel, Quito 170135"};
    Integer[] tiendafoto={R.drawable.tienda1,R.drawable.tienda2, R.drawable.tienda3,
            R.drawable.tienda4};
    String[] tiendahorario={"9:30 -20:30","9:00 - 21:30", "Martes - Sabado 10h30-17h",
    "Lunes - Domingo 8h - 20h"};

    String[] restaurantes={"Hamburguesas Rusty","Kobe Sushi Bar", "Peters","Restaurante Manabiche"};
    LatLng[] restpos = {new LatLng(-0.197187, -78.436879),new LatLng(-0.199218,-78.437293),
    new LatLng(-0.161936, -78.478883), new LatLng(-0.125991, -78.474126)};
    String[] restdir={"Antara Plaza Gourmet, Diego de Robles, Quito 170902","Paseo San Francisco Cumbaya",
    "Av. de los Shyris N34-209 y Río Coca, Av. de los Shyris N34-209, Quito", "Avenida 6 de Diciembre, Quito 170138"};
    Integer[] restfoto={R.drawable.restaurante1,R.drawable.restaurante2,
            R.drawable.restaurante3,R.drawable.restaurante4};
    String[] resthorario={"10:00 - 20:00","10:00 - 20:30", "Martes - Domingo 12h - 22h",
    "Martes - Domingo 10h - 20h"};


}
