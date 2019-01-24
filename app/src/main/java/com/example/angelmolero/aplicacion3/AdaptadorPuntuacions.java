package com.example.angelmolero.aplicacion3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

/*Un adaptadorés un mecanisme estàndard en Android que ens permet crear
* una serie de vistes que han de ser mostrades dins un contenidor.
* Amb RecyclerView s'ha d'heretar de la classe RecyclerView.Adapter
* per crear l'adaptador*/
public class AdaptadorPuntuacions extends RecyclerView.Adapter<AdaptadorPuntuacions.ViewHolder> {
    private LayoutInflater inflador; //Crea Layouts a partir del XML
    private Vector<String> llista; //Llista de puntuacions
    private int cont = 0;
    private Context context;

    protected View.OnClickListener onClickListener;

    //En el constructor s'inicialitza el conjunt de dades a mostrar
    public AdaptadorPuntuacions(Context context, Vector<String> llista) {
        //Un inflator permetrà posteriorment crear una vista a partir
        //del seu XML
        inflador = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.llista = llista;
        this.context=context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titol, subtitol;
        public ImageView icono;

        public ViewHolder(View itemView) {
            super(itemView);
            titol = (TextView) itemView.findViewById(R.id.titol);
            subtitol = (TextView) itemView.findViewById(R.id.subtitol);
            icono = (ImageView) itemView.findViewById(R.id.icono);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflador.inflate(R.layout.element_puntuacio, parent, false);
     //   Toast.makeText(context, "Cont "+(++cont), Toast.LENGTH_SHORT).show();
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titol.setText(llista.get(position));
        switch(Math.round((float)Math.random()*3)) {
            case 0:
                holder.icono.setImageResource(R.drawable.asteroide1);
                break;
            case 1:
                holder.icono.setImageResource(R.drawable.asteroide2);
                break;
            default:
                holder.icono.setImageResource(R.drawable.asteroide3);
                break;
        }
        Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
    }

    //Indica el número total d'elements a visualizar
    @Override
    public int getItemCount() {
        return llista.size();
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
