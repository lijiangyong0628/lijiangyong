package com.ljy.map.ui;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.LocalSearchInfo;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.li.ljymap.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class InitActivity extends Activity{

    private MapView mMapView = null;  
    private EditText search_edittext;
    private PoiSearch mPoiSearch;
    private MKOfflineMap mOfflineMap;
    
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);   
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());  
        setContentView(R.layout.init_activity);  
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
        //获取地图控件引用  
        mMapView = (MapView) findViewById(R.id.bmapView);  
        search_edittext = (EditText) findViewById(R.id.search_edittext);
        
        //初始化离线地图
        mOfflineMap = new MKOfflineMap();
     // 传入接口事件，离线地图更新会触发该回调
//        mOfflineMap.init(this);
        //test map
        
    }  
    
    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
		
		@Override
		public void onGetPoiResult(PoiResult arg0) {
			
		}
		
		@Override
		public void onGetPoiDetailResult(PoiDetailResult arg0) {
			
		}
	};
	
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
        mMapView.onDestroy();  
        mPoiSearch.destroy();
    }  
    
    @Override  
    protected void onResume() {  
        super.onResume();  
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
        mMapView.onResume();  
    }  
    
    public void onMyPositionClick(View v){
    	
    }
    
    public void onSearchClick(View v){
    	String str = search_edittext.getText().toString();
//    	mPoiSearch.searchInCity((new PoiCitySearchOption()).city(str));
//    	LocalSearchInfo info =  new LocalSearchInfo();
//    	info.ak = "S8POp8dRKazriIHekeHg7t66";
//    	info.geoTableId = 7535062;
//    	info.tags = "";
//    	info.q = "黄浦江";
//    	info.region = str;
//    	CloudManager.getInstance().localSearch(info);
    	
    }
    
    @Override  
    protected void onPause() {  
        super.onPause();  
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
        mMapView.onPause();  
    }
}
